package com.antsfamily.biketrainer.presentation.createworkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.createworkout.model.WorkoutType
import com.antsfamily.domain.model.IndexedWorkoutStep
import com.antsfamily.domain.model.WorkoutStep
import com.antsfamily.domain.usecase.workout.SaveWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateWorkoutUiState.Content.Empty)
    val state: StateFlow<CreateWorkoutUiState> = _state

    private val _navigateBackEvent = MutableSharedFlow<Unit>()
    val navigateBackEvent: SharedFlow<Unit> = _navigateBackEvent.asSharedFlow()

    private val _showWorkoutNameDialogEvent = MutableSharedFlow<String?>()
    val showWorkoutNameDialogEvent: SharedFlow<String?> = _showWorkoutNameDialogEvent.asSharedFlow()

    private val _setOneStepDialogVisibilityEvent = MutableSharedFlow<Boolean>()
    val setOneStepDialogVisibilityEvent: SharedFlow<Boolean> =
        _setOneStepDialogVisibilityEvent.asSharedFlow()

    private val _editOneStepDialogEvent = MutableSharedFlow<IndexedWorkoutStep?>()
    val editOneStepDialogEvent: SharedFlow<IndexedWorkoutStep?> =
        _editOneStepDialogEvent.asSharedFlow()

    private val _setWarmUpDialogVisibilityEvent = MutableSharedFlow<Boolean>()
    val setWarmUpDialogVisibilityEvent: SharedFlow<Boolean> =
        _setWarmUpDialogVisibilityEvent.asSharedFlow()

    private val _editWarmUpDialogEvent = MutableSharedFlow<IndexedWorkoutStep?>()
    val editWarmUpDialogEvent: SharedFlow<IndexedWorkoutStep?> =
        _editWarmUpDialogEvent.asSharedFlow()

    private val _setIntervalDialogVisibilityEvent = MutableSharedFlow<Boolean>()
    val setIntervalDialogVisibilityEvent: SharedFlow<Boolean> =
        _setIntervalDialogVisibilityEvent.asSharedFlow()

    private val _editIntervalDialogEvent = MutableSharedFlow<IndexedWorkoutStep?>()
    val editIntervalDialogEvent: SharedFlow<IndexedWorkoutStep?> =
        _editIntervalDialogEvent.asSharedFlow()

    private val _setCoolDownDialogVisibilityEvent = MutableSharedFlow<Boolean>()
    val setCoolDownDialogVisibilityEvent: SharedFlow<Boolean> =
        _setCoolDownDialogVisibilityEvent.asSharedFlow()

    private val _editCoolDownDialogEvent = MutableSharedFlow<IndexedWorkoutStep?>()
    val editCoolDownDialogEvent: SharedFlow<IndexedWorkoutStep?> =
        _editCoolDownDialogEvent.asSharedFlow()

    fun onWorkoutChipClick(type: WorkoutType) = viewModelScope.launch {
        when (type) {
            WorkoutType.WarmUp -> _setWarmUpDialogVisibilityEvent.emit(true)
            WorkoutType.OneStep -> _setOneStepDialogVisibilityEvent.emit(true)
            WorkoutType.Intervals -> _setIntervalDialogVisibilityEvent.emit(true)
            WorkoutType.CoolDown -> _setCoolDownDialogVisibilityEvent.emit(true)
        }
    }

    fun onWorkoutEditClick(step: IndexedWorkoutStep) = viewModelScope.launch {
        val selectedStep = _state.value.steps.firstOrNull { it.index == step.index }
        selectedStep?.let {
            when (it.step) {
                is WorkoutStep.WarmUp -> _editWarmUpDialogEvent.emit(it)
                is WorkoutStep.OneStep -> _editOneStepDialogEvent.emit(it)
                is WorkoutStep.Intervals -> _editIntervalDialogEvent.emit(it)
                is WorkoutStep.CoolDown -> _editCoolDownDialogEvent.emit(it)
            }
        }
    }

    fun onTitleClick(currentTile: String) = viewModelScope.launch {
        _showWorkoutNameDialogEvent.emit(currentTile)
    }

    fun onTitleChange(title: String) = viewModelScope.launch {
        _showWorkoutNameDialogEvent.emit(null)
        _state.update { it.copy(name = title.trim()) }
    }

    fun onWorkoutStepDelete(step: IndexedWorkoutStep) {
        val updatedWorkoutSteps = _state.value.steps.filter { it.index != step.index }
        _state.update { state ->
            state.copy(
                steps = reindexWorkoutSteps(updatedWorkoutSteps),
                totalDuration = updatedWorkoutSteps.sumOf { it.step.getTotalDuration() }
            )
        }
    }

    fun onOneStepAdd(step: WorkoutStep.OneStep) = viewModelScope.launch {
        handleNewWorkoutStep(step)
    }

    fun onOneStepChange(id: Int, step: WorkoutStep.OneStep) = viewModelScope.launch {
        handleWorkoutStepChanges(id, step)
    }

    fun onWarmUpStepAdd(step: WorkoutStep.WarmUp) = viewModelScope.launch {
        handleNewWorkoutStep(step)
    }

    fun onWarmUpStepChange(id: Int, step: WorkoutStep.WarmUp) = viewModelScope.launch {
        handleWorkoutStepChanges(id, step)
    }

    fun onIntervalsAdd(step: WorkoutStep.Intervals) = viewModelScope.launch {
        handleNewWorkoutStep(step)
    }

    fun onIntervalsChange(id: Int, step: WorkoutStep.Intervals) = viewModelScope.launch {
        handleWorkoutStepChanges(id, step)
    }

    fun onCoolDownStepAdd(step: WorkoutStep.CoolDown) = viewModelScope.launch {
        handleNewWorkoutStep(step)
    }

    fun onCoolDownStepChange(id: Int, step: WorkoutStep.CoolDown) = viewModelScope.launch {
        handleWorkoutStepChanges(id, step)
    }

    private fun handleWorkoutStepChanges(id: Int, step: WorkoutStep) = viewModelScope.launch {
        val indexedStep = IndexedWorkoutStep(index = id, step = step)
        updateWorkoutSteps(indexedStep, false)
        when (step) {
            is WorkoutStep.CoolDown -> _editCoolDownDialogEvent.emit(null)
            is WorkoutStep.Intervals -> _editIntervalDialogEvent.emit(null)
            is WorkoutStep.OneStep -> _editOneStepDialogEvent.emit(null)
            is WorkoutStep.WarmUp -> _editWarmUpDialogEvent.emit(null)
        }
    }

    private fun handleNewWorkoutStep(step: WorkoutStep) = viewModelScope.launch {
        val workoutSize = _state.value.steps.size
        val indexedStep = IndexedWorkoutStep(index = workoutSize.plus(1), step = step)
        updateWorkoutSteps(indexedStep, true)
        when (step) {
            is WorkoutStep.CoolDown -> _setCoolDownDialogVisibilityEvent.emit(false)
            is WorkoutStep.Intervals -> _setIntervalDialogVisibilityEvent.emit(false)
            is WorkoutStep.OneStep -> _setOneStepDialogVisibilityEvent.emit(false)
            is WorkoutStep.WarmUp -> _setWarmUpDialogVisibilityEvent.emit(false)
        }
    }

    private fun updateWorkoutSteps(newStep: IndexedWorkoutStep, isItemNew: Boolean) {
        val currentSteps = _state.value.steps
        val updatedSteps = if (isItemNew) {
            currentSteps.plus(newStep)
        } else {
            currentSteps.map { if (it.index == newStep.index) newStep else it }
        }
        val totalDuration = updatedSteps.sumOf { it.step.getTotalDuration() }
        _state.update { state ->
            state.copy(
                steps = updatedSteps,
                totalDuration = totalDuration,
                isSafeWorkoutButtonEnable = updatedSteps.isNotEmpty() && !state.name.isNullOrBlank() && totalDuration > 600
            )
        }
    }

    private fun reindexWorkoutSteps(items: List<IndexedWorkoutStep>): List<IndexedWorkoutStep> {
        return items.mapIndexed { index, it -> it.copy(index = index) }
    }

    fun onSaveClick() = viewModelScope.launch {
        _state.update { it.copy(isSafeWorkoutLoadingVisible = true) }
        try {
            _state.value.name?.let {
                saveWorkoutUseCase(it, _state.value.steps)
            }
            _state.value = CreateWorkoutUiState.Content.Empty
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
        } finally {
            _state.update { it.copy(isSafeWorkoutLoadingVisible = false) }
        }
    }
}
