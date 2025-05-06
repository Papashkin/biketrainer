package com.antsfamily.domain.usecase.workout

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import javax.inject.Inject

class SaveWorkoutUseCase @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val workoutRepository: WorkoutRepository
) : BaseUseCase<SaveWorkoutUseCase.Params, Result<Unit, Error>>() {

    override suspend fun run(params: Params): Result<Unit, Error> = try {
        val profile: Profile? = profilesRepository.getSelectedProfileName()?.let {
            profilesRepository.getProfile(it)
        }

        profile?.let {
            val program = Program(
                title = params.name,
                data = params.data,
            )
            Result.Success(workoutRepository.insertProgram(program))
        } ?: Result.Failure(Error("Selected profile is absent"))
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail :("))
    }

    data class Params(
        val name: String,
        val data: List<ProgramData>
    )
}
