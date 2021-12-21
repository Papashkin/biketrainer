package com.antsfamily.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
import com.antsfamily.domain.usecase.workout.GetWorkoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
    fun `get program success`() = runBlockingTest {
        Mockito.`when`(repository.getProgram("Test1"))
            .thenReturn(Program("Test1", listOf(), "Mocker"))

        val program = getProgramUseCase.run("Test1")

        assert((program as? Result.Success)?.successData?.username == "Mocker")
    }

    @Test
    fun `get program failure`() = runBlockingTest {
        Mockito.`when`(repository.getProgram("Test1"))
            .thenThrow(RuntimeException("error occurred"))

        val program = getProgramUseCase.run("Test1")

        assert((program as? Result.Failure)?.errorData?.message == "Epic fail")
    }
}
