package com.antsfamily.biketrainer.presentation.createworkout

import com.antsfamily.domain.model.IndexedWorkoutStep

sealed class CreateWorkoutUiState {
    data class Content(
        val name: String?,
        val steps: List<IndexedWorkoutStep>,
        val totalDuration: Int,
        val isSafeWorkoutButtonEnable: Boolean,
        val isSafeWorkoutLoadingVisible: Boolean,
    ) : CreateWorkoutUiState() {

        companion object {
            val Empty = Content(
                name = null,
                steps = listOf(),
                totalDuration = 0,
                isSafeWorkoutButtonEnable = false,
                isSafeWorkoutLoadingVisible = false
            )
        }
    }

    data object Loading : CreateWorkoutUiState()
}