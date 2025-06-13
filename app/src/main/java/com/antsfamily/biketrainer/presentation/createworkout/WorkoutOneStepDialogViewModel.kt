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
class WorkoutOneStepDialogViewModel @Inject constructor() : ViewModel() {

    data class State(
        val power: Int? = null,
        val duration: Duration = Duration.Empty,
        val isConfirmButtonEnabled: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private val _navigateBackEvent = MutableSharedFlow<WorkoutStep.OneStep>()
    val navigateBackEvent: SharedFlow<WorkoutStep.OneStep> = _navigateBackEvent.asSharedFlow()

    fun initialize(step: WorkoutStep.OneStep?) = viewModelScope.launch {
        delay(200)
        step?.let {
            _state.update {
                it.copy(power = step.power, duration = step.duration.orEmpty())
            }
            validateStep()
        }
    }

    fun onConfirmClick() = viewModelScope.launch {
        val stateValue = _state.value
        val step = WorkoutStep.OneStep(
            power = stateValue.power.orZero(),
            duration = stateValue.duration,
        )
        _navigateBackEvent.emit(step)
    }

    fun onPowerChanged(power: Int?) {
        if (power == null) return
        _state.update { it.copy(power = power) }
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
        val isWorkPowerValid = _state.value.power.orZero() > 50
        val isWorkDurationValid = !_state.value.duration.isZero
        _state.update {
            it.copy(isConfirmButtonEnabled = isWorkPowerValid && isWorkDurationValid)
        }
    }
}