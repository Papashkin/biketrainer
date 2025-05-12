package com.antsfamily.biketrainer.presentation.createworkout

import com.antsfamily.biketrainer.ui.createworkout.model.IndexedWorkoutStep

sealed class CreateWorkoutUiState {
    data class Content(
        val name: String?,
        val steps: List<IndexedWorkoutStep>,
        val totalDuration: Int,
        val isSafeWorkoutButtonEnable: Boolean,
        val isSafeWorkoutLoadingVisible: Boolean,
    ): CreateWorkoutUiState() {

        companion object {
            val Empty = Content(null, listOf(), 0, false, false)
        }
    }
    data object Loading: CreateWorkoutUiState()
}