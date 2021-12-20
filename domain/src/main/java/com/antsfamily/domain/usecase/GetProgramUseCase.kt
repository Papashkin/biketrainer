package com.antsfamily.domain.usecase

import com.antsfamily.data.local.repositories.ProgramsRepository
import com.antsfamily.data.model.program.Program
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import javax.inject.Inject

class GetProgramUseCase @Inject constructor(
    private val programsRepository: ProgramsRepository
) : BaseUseCase<String, Result<Program, Error>>() {

    override suspend fun run(params: String): Result<Program, Error> = try {
        val program: Program? = programsRepository.getProgram(params)
        program?.let {
            Result.Success(it)
        } ?: Result.Failure(Error("There is no program with mentioned title"))
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}
