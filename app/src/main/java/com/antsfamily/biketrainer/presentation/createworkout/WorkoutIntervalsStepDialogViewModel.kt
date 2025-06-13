package com.antsfamily.biketrainer.presentation.createworkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.util.orEmpty
import com.antsfamily.domain.antservice.orZero
import com.antsfamily.domain.model.Duration
import com.antsfamily.domain.model.WorkoutStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutIntervalsStepDialogViewModel @Inject constructor() : ViewModel() {

    data class State(
        val workPower: Int? = null,
        val workDuration: Duration? = null,
        val restPower: Int? = null,
        val restDuration: Duration? = null,
        val reps: Int = 0,
        val isConfirmButtonEnabled: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private val _closeDialogWithData = MutableSharedFlow<WorkoutStep.Intervals>()
    val closeDialogWithData: SharedFlow<WorkoutStep.Intervals> = _closeDialogWithData.asSharedFlow()

    fun initialize(step: WorkoutStep.Intervals?) = viewModelScope.launch {
        delay(200)
        step?.let {
            _state.update {
                it.copy(
                    workPower = step.power,
                    workDuration = step.powerDuration.orEmpty(),
                    restPower = step.rest,
                    restDuration = step.restDuration.orEmpty(),
                    reps = step.reps.orZero()
                )
            }
            validateStep()
        }
    }

    fun onConfirmClick() = viewModelScope.launch {
        val stateValue = _state.value
        val step = WorkoutStep.Intervals(
            power = stateValue.workPower.orZero(),
            powerDuration = stateValue.workDuration.orEmpty(),
            rest = _state.value.restPower.orZero(),
            restDuration = _state.value.restDuration.orEmpty(),
            reps = _state.value.reps
        )
        _closeDialogWithData.emit(step)
    }

    fun onWorkPowerChanged(power: Int?) {
        if (power == null) return
        _state.update { it.copy(workPower = power) }
        validateStep()
    }

    fun onWorkDurationChanged(duration: Duration) {
        _state.update { it.copy(workDuration = duration) }
        validateStep()
    }

    fun onRestPowerChanged(power: Int?) {
        if (power == null) return
        _state.update { it.copy(restPower = power) }
        validateStep()
    }

    fun onRestDurationChanged(duration: Duration) {
        _state.update { it.copy(restDuration = duration) }
        validateStep()
    }

    fun onRepsDecrease() {
        val reps = _state.value.reps.minus(1)
        if (reps < 0) return
        _state.update { it.copy(reps = reps) }
        validateStep()
    }

    fun onRepsIncrease() {
        val reps = _state.value.reps.plus(1)
        _state.update { it.copy(reps = reps) }
        validateStep()
    }

    fun invalidateState() {
        _state.value = State()
    }

    private fun validateStep() {
        val isWorkPowerValid =
            _state.value.workPower.orZero() > 50 && _state.value.workPower.orZero() > _state.value.restPower.orZero()
        val isWorkDurationValid = _state.value.workDuration?.isZero == false
        val isRestPowerValid =
            _state.value.restPower.orZero() > 50 && _state.value.workPower.orZero() > _state.value.restPower.orZero()
        val isRestDurationValid = _state.value.restDuration?.isZero == false
        val isRepsValid = _state.value.reps > 0
        _state.update {
            it.copy(isConfirmButtonEnabled = isWorkPowerValid && isWorkDurationValid && isRestPowerValid && isRestDurationValid && isRepsValid)
        }
    }
}