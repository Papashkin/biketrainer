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
class WorkoutWarmUpStepDialogViewModel @Inject constructor() : ViewModel() {

    data class State(
        val startPower: Int? = null,
        val endPower: Int? = null,
        val duration: Duration = Duration.Empty,
        val isConfirmButtonEnabled: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _navigateBackEvent = MutableSharedFlow<WorkoutStep.WarmUp>()
    val navigateBackEvent: SharedFlow<WorkoutStep.WarmUp> = _navigateBackEvent.asSharedFlow()

    fun initialize(step: WorkoutStep.WarmUp?) = viewModelScope.launch {
        delay(200)
        step?.let {
            _state.update {
                it.copy(
                    startPower = step.startPower,
                    endPower = step.endPower,
                    duration = step.duration.orEmpty()
                )
            }
            validateStep()
        }
    }

    fun onConfirmClick() = viewModelScope.launch {
        val stateValue = _state.value
        val step = WorkoutStep.WarmUp(
            startPower = stateValue.startPower.orZero(),
            endPower = stateValue.endPower.orZero(),
            duration = stateValue.duration,
        )
        _navigateBackEvent.emit(step)
    }

    fun onStartPowerChanged(power: Int?) {
        if (power == null) return
        _state.update { it.copy(startPower = power) }
        validateStep()
    }

    fun onEndPowerChanged(power: Int?) {
        if (power == null) return
        _state.update { it.copy(endPower = power) }
        validateStep()
    }

    fun onDurationChanged(duration: Duration) {
        _state.update { it.copy(duration = duration) }
        validateStep()
    }

    fun invalidateState() {
        _state.value = State()
    }

    private fun validateStep() {
        val isWorkPowerValid =
            _state.value.startPower.orZero() > 50 && _state.value.startPower.orZero() < _state.value.endPower.orZero()
        val isWorkDurationValid = !_state.value.duration.isZero
        _state.update {
            it.copy(isConfirmButtonEnabled = isWorkPowerValid && isWorkDurationValid)
        }
    }
}