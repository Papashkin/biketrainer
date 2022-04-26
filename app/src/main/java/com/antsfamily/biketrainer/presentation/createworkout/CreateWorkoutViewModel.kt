package com.antsfamily.biketrainer.presentation.createworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.navigation.CreateProgramToAddInterval
import com.antsfamily.biketrainer.navigation.CreateProgramToAddSegment
import com.antsfamily.biketrainer.navigation.CreateProgramToAddStairs
import com.antsfamily.biketrainer.presentation.Event
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.biketrainer.presentation.postEvent
import com.antsfamily.biketrainer.ui.util.StaticFields.LOTTIE_ANIMATION_SUCCESS
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.data.model.workouts.WorkoutIntervalParams
import com.antsfamily.data.model.workouts.WorkoutSegmentParams
import com.antsfamily.data.model.workouts.WorkoutStairsParams
import com.antsfamily.domain.Result
import com.antsfamily.domain.usecase.workout.SaveWorkoutUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.util.*

class CreateWorkoutViewModel @AssistedInject constructor(
    private val saveWorkoutUseCase: SaveWorkoutUseCase
) : StatefulViewModel<CreateWorkoutViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(): CreateWorkoutViewModel
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Nothing,
        val isEmptyBarChartVisible: Boolean = true,
        val isBarChartVisible: Boolean = false,
        val programName: String? = null,
        val programNameError: String? = null,
        val workoutData: List<ProgramData> = emptyList(),
        val workoutError: String? = null
    )

    private val _clearInputFieldsEvent = MutableLiveData<Event<Unit>>()
    val clearInputFieldsEvent: LiveData<Event<Unit>>
        get() = _clearInputFieldsEvent

    private var dataSet: MutableList<ProgramData> = mutableListOf()

    fun onBackClick() {
        navigateBack()
    }

    fun onProgramNameChange() {
        changeState { it.copy(programNameError = null) }
    }

    fun onIntervalsClick() {
        navigateTo(CreateProgramToAddInterval)
    }

    fun onSegmentClick() {
        navigateTo(CreateProgramToAddSegment)
    }

    fun onStairsClick() {
        navigateTo(CreateProgramToAddStairs)
    }

    fun onCreateClick(name: String) {
        if (isValid(name)) {
            saveProgram(name)
        }
    }

    fun onWorkoutSuccessAnimationEnd() {
        refreshState()
        _clearInputFieldsEvent.postEvent()
    }

    fun onSegmentAdd(segment: WorkoutSegmentParams?) {
        segment?.let {
            setSegment(it)
            updateChart()
        }
    }

    fun onIntervalAdd(interval: WorkoutIntervalParams?) {
        interval?.let {
            setInterval(it)
            updateChart()
        }
    }

    fun onStairsAdd(stairs: WorkoutStairsParams?) {
        stairs?.let {
            setStairs(it)
            updateChart()
        }
    }

    private fun setSegment(workout: WorkoutSegmentParams) {
        dataSet.add(ProgramData(workout.power, workout.duration))
        changeState { state ->
            state.copy(
                workoutData = state.workoutData.plus(ProgramData(workout.power, workout.duration)),
            )
        }
    }

    private fun setInterval(workout: WorkoutIntervalParams) {
        for (interval in 0 until workout.times) {
            setSegment(WorkoutSegmentParams(workout.peakPower, workout.peakDuration))
            setSegment(WorkoutSegmentParams(workout.restPower, workout.restDuration))
        }
    }

    private fun setStairs(workout: WorkoutStairsParams) {
        val stepPower = (workout.endPower - workout.startPower) / workout.steps.minus(1)
        val durationForEachStep = workout.duration / workout.steps
        for (index in 0 until workout.steps) {
            setSegment(
                WorkoutSegmentParams(
                    workout.startPower + stepPower.times(index),
                    durationForEachStep
                )
            )
        }
    }

    private fun updateChart() {
        changeState {
            it.copy(
                workoutData = dataSet,
                isEmptyBarChartVisible = dataSet.isEmpty(),
                isBarChartVisible = dataSet.isNotEmpty(),
                workoutError = null
            )
        }
    }

    private fun isValid(name: String): Boolean {
        val isNameValid = name.isNotBlank()
        val isWorkoutValid = dataSet.isNotEmpty()

        if (!isNameValid) {
            changeState { it.copy(programNameError = "Program name is invalid") }
        }
        if (!isWorkoutValid) {
            changeState { it.copy(workoutError = "Program should contain at least 1 segment") }
        }

        return isNameValid && isWorkoutValid
    }

    private fun saveProgram(name: String) = viewModelScope.launch {
        showLoading()
        saveWorkoutUseCase(
            SaveWorkoutUseCase.Params(Random().nextInt(), name, dataSet),
            ::handleSaveProgramResult
        )
    }

    private fun handleSaveProgramResult(result: Result<Unit, Error>) {
        when (result) {
            is Result.Success -> handleSaveWorkoutSuccessResult()
            is Result.Failure -> handleSaveWorkoutErrorResult()
        }
        hideLoading()
    }

    private fun handleSaveWorkoutErrorResult() {
        hideLoading()
        showErrorSnackbar("Something went wrong. Please try it again later or change the name of the program")
    }
    private fun handleSaveWorkoutSuccessResult() {
        changeState { it.copy(loadingState = LoadingState.Success(LOTTIE_ANIMATION_SUCCESS)) }
    }

    private fun refreshState() {
        changeState {
            it.copy(
                loadingState = LoadingState.Nothing,
                isEmptyBarChartVisible = true,
                isBarChartVisible = false,
                programName = null,
                programNameError = null,
                workoutData = emptyList(),
                workoutError = null
            )
        }
    }

    private fun showLoading() {
        changeState { it.copy(loadingState = LoadingState.Loading) }
    }

    private fun hideLoading() {
        changeState { it.copy(loadingState = LoadingState.Nothing) }
    }
}
