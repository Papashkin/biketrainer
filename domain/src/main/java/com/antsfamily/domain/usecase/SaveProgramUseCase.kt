package com.antsfamily.domain.usecase

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.local.repositories.ProgramsRepository
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import javax.inject.Inject

class SaveProgramUseCase @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val programsRepository: ProgramsRepository
) : BaseUseCase<SaveProgramUseCase.Params, Result<Unit, Error>>() {

    override suspend fun run(params: Params): Result<Unit, Error> = try {
        val profile: Profile? = profilesRepository.getSelectedProfileName()?.let {
            profilesRepository.getProfile(it)
        }

        profile?.let {
            val program = Program(
                title = params.name,
                data = params.data,
                username = it.name
            )
            Result.Success(programsRepository.insertProgram(program))
        } ?: Result.Failure(Error("Selected profile is absent"))
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail :("))
    }

    data class Params(
        val id: Int,
        val name: String,
        val data: List<ProgramData>
    )
}
