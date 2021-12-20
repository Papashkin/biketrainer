package com.antsfamily.domain.usecase

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.model.profile.ProfileWithPrograms
import com.antsfamily.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeToProfileWithProgramsUseCase @Inject constructor(
    private val repository: ProfilesRepository
) : FlowUseCase<String, ProfileWithPrograms>() {

    override fun run(params: String): Flow<ProfileWithPrograms> =
        repository.getProfileWithPrograms(params)
}
