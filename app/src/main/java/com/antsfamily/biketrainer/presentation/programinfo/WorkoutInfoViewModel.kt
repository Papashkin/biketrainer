package com.antsfamily.biketrainer.presentation.programinfo

import com.antsfamily.biketrainer.navigation.ProgramInfoToScan
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.biketrainer.ui.util.StaticFields.LOTTIE_ANIMATION_DELETE
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.domain.Result
import com.antsfamily.domain.usecase.workout.GetWorkoutUseCase
import com.antsfamily.domain.usecase.workout.RemoveWorkoutUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class WorkoutInfoViewModel @AssistedInject constructor(
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val removeWorkoutUseCase: RemoveWorkoutUseCase,
    @Assisted private val workoutName: String
) : StatefulViewModel<WorkoutInfoViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(workoutName: String): WorkoutInfoViewModel
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Nothing,
        val program: List<ProgramData> = emptyList(),
        val programName: String? = null,
        val duration: String? = null,
        val maxPower: String? = null,
        val avgPower: String? = null
    )

    init {
        getWorkout(workoutName)
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onEditClick() {
        //TODO create navigation to edit workout page / create workout page
    }

    fun onDeleteClick() {
        deleteWorkout()
    }

    fun onRunWorkoutClick() {
        state.value?.programName?.let {
            navigateTo(ProgramInfoToScan(it))
        }
    }

    fun onRemoveWorkoutAnimationEnd() {
        navigateBack()
    }

    private fun getWorkout(name: String) {
        getWorkoutUseCase(name, ::handleGetWorkoutResult)
    }

    private fun handleGetWorkoutResult(result: Result<Program, Error>) {
        hideLoading()
        when (result) {
            is Result.Success -> handleGetWorkoutSuccess(result.successData)
            is Result.Failure -> handleFailure(result.errorData)
        }
    }

    private fun handleGetWorkoutSuccess(program: Program) {
        changeState { state ->
            state.copy(
                program = program.data,
                programName = program.title,
                duration = program.data.sumOf { it.duration }.fullTimeFormat(),
                maxPower = program.data.maxOf { it.power }.toString(),
                avgPower = program.data.map { it.power }.sum().div(program.data.size).toString()
            )
        }
    }

    private fun handleFailure(data: Error) {
        hideLoading()
        showErrorSnackbar(data.message)
    }

    private fun deleteWorkout() {
        showLoading()
        removeWorkoutUseCase(workoutName, ::handleRemoveWorkoutResult)
    }

    private fun handleRemoveWorkoutResult(result: Result<Unit, Error>) {
        when (result) {
            is Result.Success -> handleRemoveWorkoutSuccess()
            is Result.Failure -> handleFailure(result.errorData)
        }
    }

    private fun handleRemoveWorkoutSuccess() {
        changeState { it.copy(loadingState = LoadingState.Success(LOTTIE_ANIMATION_DELETE)) }
    }

    private fun showLoading() {
        changeState { it.copy(loadingState = LoadingState.Loading) }
    }

    private fun hideLoading() {
        changeState { it.copy(loadingState = LoadingState.Nothing) }
    }
}
