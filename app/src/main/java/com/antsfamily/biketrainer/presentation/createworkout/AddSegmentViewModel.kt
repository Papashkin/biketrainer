package com.antsfamily.biketrainer.presentation.createworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.antsfamily.biketrainer.presentation.Event
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.data.model.workouts.WorkoutSegmentParams
import com.antsfamily.domain.antservice.orZero
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.math.BigDecimal

class AddSegmentViewModel @AssistedInject constructor() :
    StatefulViewModel<AddSegmentViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(): AddSegmentViewModel
    }

    data class State(
        val power: BigDecimal? = null,
        val powerError: String? = null,
        val duration: Long = 0,
        val durationError: String? = null,
    )

    private val _setSegmentResult = MutableLiveData<Event<WorkoutSegmentParams>>()
    val setSegmentResult: LiveData<Event<WorkoutSegmentParams>>
        get() = _setSegmentResult

    fun onAddClick(power: Int, duration: Long) {
        if (isValid(power, duration)) {
            _setSegmentResult.postValue(Event(WorkoutSegmentParams(power, duration)))
            navigateBack()
        }
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onPowerTextChange() {
        changeState { it.copy(powerError = null) }
    }

    fun onDurationChange() {
        changeState { it.copy(durationError = null) }
    }

    private fun isValid(power: Int?, duration: Long): Boolean {
        val isPowerValid = power.orZero() > 0
        val isDurationValid = duration > MINIMUM_DURATION

        if (!isDurationValid) {
            changeState { it.copy(durationError = "Duration is invalid. Duration should be at least more than $MINIMUM_DURATION sec") }
        }
        if (!isPowerValid) {
            changeState { it.copy(powerError = "Power is invalid") }
        }

        return isPowerValid && isDurationValid
    }

    companion object {
        private const val MINIMUM_DURATION = 10
    }
}
