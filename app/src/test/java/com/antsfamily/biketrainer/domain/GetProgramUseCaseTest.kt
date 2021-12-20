package com.antsfamily.biketrainer.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.biketrainer.MainCoroutineRule
import com.antsfamily.biketrainer.data.local.repositories.ProgramsRepository
import com.antsfamily.biketrainer.data.models.program.Program
import com.antsfamily.biketrainer.domain.usecase.GetProgramUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetProgramUseCaseTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository: ProgramsRepository = Mockito.mock(ProgramsRepository::class.java)

    private val getProgramUseCase = GetProgramUseCase(repository)

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
