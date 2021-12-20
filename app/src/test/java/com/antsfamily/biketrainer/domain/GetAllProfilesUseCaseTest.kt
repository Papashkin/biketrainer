package com.antsfamily.biketrainer.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.biketrainer.MainCoroutineRule
import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.data.models.profile.Profile
import com.antsfamily.biketrainer.domain.usecase.GetAllProfilesUseCase
import com.antsfamily.biketrainer.util.orFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetAllProfilesUseCaseTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository: ProfilesRepository = Mockito.mock(ProfilesRepository::class.java)

    private val getAllProfilesUseCase = GetAllProfilesUseCase(repository)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `get all profile success`() = runBlockingTest {
        Mockito.`when`(repository.getAllProfiles()).thenReturn(listOf(
            Profile("Test", 22, "male", 170.0f, 70.0f)
        ))

        val profiles = getAllProfilesUseCase.run(Unit)

        assert((profiles as? Result.Success)?.successData?.size == 1)
        assert((profiles as? Result.Success)?.successData?.firstOrNull()?.name == "Test")
    }

    @Test
    fun `get empty profiles success`() = runBlockingTest {
        Mockito.`when`(repository.getAllProfiles()).thenReturn(listOf())

        val profiles = getAllProfilesUseCase.run(Unit)

        assert((profiles as? Result.Success)?.successData?.isEmpty().orFalse())
    }

    @Test
    fun `get all profiles failure`() = runBlockingTest {
        Mockito.`when`(repository.getAllProfiles()).thenThrow(RuntimeException("error occurred"))

        val profiles = getAllProfilesUseCase.run(Unit)

        assert((profiles as? Result.Failure)?.errorData?.message == "Epic fail")
    }
}
