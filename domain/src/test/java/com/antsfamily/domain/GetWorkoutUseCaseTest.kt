package com.antsfamily.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.repository.WorkoutRepository
import com.antsfamily.domain.usecase.workout.GetWorkoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetWorkoutUseCaseTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository: WorkoutRepository = Mockito.mock(WorkoutRepository::class.java)

    private val getProgramUseCase = GetWorkoutUseCase(repository)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `get program success`() = runTest {
        Mockito.`when`(repository.getWorkoutByName("Test1"))
            .thenReturn(Workout("Test1", listOf()))

        val program = getProgramUseCase.run("Test1")

        assert((program as? Result.Success)?.successData?.title == "Test1")
    }

    @Test
    fun `get program failure`() = runTest {
        Mockito.`when`(repository.getWorkoutByName("Test1"))
            .thenThrow(RuntimeException("error occurred"))

        val program = getProgramUseCase.run("Test1")

        assert((program as? Result.Failure)?.errorData?.message == "Epic fail")
    }
}
