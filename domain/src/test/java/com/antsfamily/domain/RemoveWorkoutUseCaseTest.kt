package com.antsfamily.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.repository.WorkoutRepository
import com.antsfamily.domain.usecase.workout.RemoveWorkoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoveWorkoutUseCaseTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository: WorkoutRepository = Mockito.mock(WorkoutRepository::class.java)

    private val useCase = RemoveWorkoutUseCase(repository)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `remove workout successfully`() = runTest {
        Mockito.`when`(repository.getWorkoutByName(MOCK_PROGRAM_TITLE)).thenReturn(MOCK_PROGRAM)
        Mockito.`when`(repository.removeWorkout(MOCK_PROGRAM)).thenReturn(Unit)

        val result = useCase.run(MOCK_PROGRAM_TITLE)

        assert(result is Result.Success)
    }

    @Test
    fun `remove workout with failure 1`() = runTest {
        Mockito.`when`(repository.getWorkoutByName(MOCK_PROGRAM_TITLE)).thenReturn(MOCK_PROGRAM)
        Mockito.`when`(repository.removeWorkout(MOCK_PROGRAM)).thenThrow(RuntimeException("error occurred"))

        val result = useCase.run(MOCK_PROGRAM_TITLE)

        assert((result as? Result.Failure)?.errorData?.message == MOCK_ERROR_MESSAGE)
    }

    @Test
    fun `remove workout with failure 2`() = runTest {
        Mockito.`when`(repository.getWorkoutByName(MOCK_PROGRAM_TITLE)).thenThrow(RuntimeException("error occurred"))

        val result = useCase.run(MOCK_PROGRAM_TITLE)

        assert((result as? Result.Failure)?.errorData?.message == MOCK_ERROR_MESSAGE)
    }

    companion object {
        private const val MOCK_PROGRAM_TITLE = "mock_1"
        private const val MOCK_ERROR_MESSAGE = "Epic fail :("
        private val MOCK_PROGRAM = Workout(MOCK_PROGRAM_TITLE, listOf())
    }
}
