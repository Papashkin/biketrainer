package com.antsfamily.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.domain.repository.ProfilesRepository
import com.antsfamily.domain.usecase.profile.GetSelectedProfileNameUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetSelectedProfileNameUseCaseTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository: ProfilesRepository = mock(ProfilesRepository::class.java)

    private val getSelectedProfileNameUseCase = GetSelectedProfileNameUseCase(repository)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `get selected profile success`() = runTest {
        `when`(repository.getSelectedProfileName()).thenReturn("Test")

        val profile = getSelectedProfileNameUseCase.run(Unit)

        assert((profile as? Result.Success)?.successData == "Test")
    }

    @Test
    fun `get selected profile failure`() = runTest {
        `when`(repository.getSelectedProfileName()).thenThrow(RuntimeException("error occurred"))

        val profile = getSelectedProfileNameUseCase.run(Unit)

        assert((profile as? Result.Failure)?.errorData?.message == "Epic fail")
    }
}
