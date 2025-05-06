package com.antsfamily.data.local.repositories

import com.antsfamily.data.local.database.ProgramDao
import com.antsfamily.data.model.program.Program
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(private val dao: ProgramDao) : WorkoutRepository {
    override suspend fun getAllPrograms(): List<Program> = dao.getAll()
    override val programs: Flow<List<Program>> = dao.getPrograms()
    override suspend fun getProgram(name: String): Program? = dao.getProgram(name)

    override suspend fun insertProgram(program: Program) = dao.insertProgram(program)
    override suspend fun updateProgram(program: Program) = dao.updateProgram(program)
    override suspend fun removeProgram(program: Program) = dao.deleteProgram(program)
}
