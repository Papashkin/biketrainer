package com.antsfamily.biketrainer.domain.usecase

import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.data.models.profile.ProfileWithPrograms
import com.antsfamily.biketrainer.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeToProfileWithProgramsUseCase @Inject constructor(
    private val repository: ProfilesRepository
) : FlowUseCase<String, ProfileWithPrograms>() {

    override fun run(params: String): Flow<ProfileWithPrograms> =
        repository.getProfileWithPrograms(params)
}
