package com.antsfamily.biketrainer.presentation.programinfo

import com.antsfamily.biketrainer.navigation.ProgramInfoToScan
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.domain.Result
import com.antsfamily.domain.usecase.GetProgramUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProgramInfoViewModel @AssistedInject constructor(
    private val getProgramUseCase: GetProgramUseCase
) : StatefulViewModel<ProgramInfoViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build() : ProgramInfoViewModel
    }

    data class State(
        val isLoading: Boolean = true,
        val program: List<ProgramData> = emptyList(),
        val programName: String? = null,
        val duration: String? = null,
        val maxPower: String? = null,
        val avgPower: String? = null
    )

    fun onCreate(name: String) {
        getProgram(name)
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onEditClick() {
//        navigateTo()
    }

    fun onDeleteClick() {

    }

    fun onRunWorkoutClick() {
        state.value?.programName?.let {
            navigateTo(ProgramInfoToScan(it))
        }
    }

    private fun getProgram(name: String) {
        getProgramUseCase(name, ::handleProgramResult)
    }

    private fun handleProgramResult(result: Result<Program, Error>) {
        when (result) {
            is Result.Success -> handleSuccessProgram(result.successData)
            is Result.Failure -> handleFailureProgram(result.errorData)
        }
    }

    private fun handleSuccessProgram(program: Program) {
        changeState {
            it.copy(
                isLoading = false,
                program = program.data,
                programName = program.title,
                duration = program.data.sumOf { it.duration }.fullTimeFormat(),
                maxPower = program.data.maxOf { it.power }.toString(),
                avgPower = program.data.map { it.power }.sum().div(program.data.size).toString()
            )
        }
    }

    private fun handleFailureProgram(data: Error) {

    }
}
