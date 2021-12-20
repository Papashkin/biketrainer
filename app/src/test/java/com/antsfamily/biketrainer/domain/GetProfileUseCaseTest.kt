package com.antsfamily.biketrainer.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.biketrainer.MainCoroutineRule
import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.data.models.profile.Profile
import com.antsfamily.biketrainer.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetProfileUseCaseTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository: ProfilesRepository = Mockito.mock(ProfilesRepository::class.java)

    private val getProfileUseCase = GetProfileUseCase(repository)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `get profile success`() = runBlockingTest {
        Mockito.`when`(repository.getProfile("Test")).thenReturn(
            Profile("Test", 22, "male", 170.0f, 70.0f)
        )

        val profile = getProfileUseCase.run("Test")

        assert((profile as? Result.Success)?.successData?.age == 22)
    }

    @Test
    fun `get profile not existing`() = runBlockingTest {
        val profile = getProfileUseCase.run("Unit")

        assert((profile as? Result.Success)?.successData == null)
    }

    @Test
    fun `get all profiles failure`() = runBlockingTest {
        Mockito.`when`(repository.getProfile("Test"))
            .thenThrow(RuntimeException("error occurred"))

        val profile = getProfileUseCase.run("Test")

        assert((profile as? Result.Failure)?.errorData?.message == "Epic fail")
    }
}
