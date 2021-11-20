package com.antsfamily.biketrainer.data.local.repositories

import com.antsfamily.biketrainer.data.local.database.ProgramDao
import com.antsfamily.biketrainer.data.models.program.Program
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProgramsRepositoryImpl @Inject constructor(private val dao: ProgramDao) : ProgramsRepository {
    override suspend fun getAllPrograms(): List<Program> = dao.getAll()
    override val programs: Flow<List<Program>> = dao.getPrograms()
    override suspend fun getProgram(name: String): Program? = dao.getProgram(name)
    override suspend fun getProgramsByUsername(username: String): List<Program> =
        dao.getProgramsByUsername(username)

    override suspend fun insertProgram(program: Program) = dao.insertProgram(program)
    override suspend fun updateProgram(program: Program) = dao.updateProgram(program)
    override suspend fun removeProgram(program: Program) = dao.deleteProgram(program)
}

interface ProgramsRepository {
    suspend fun getAllPrograms(): List<Program>
    val programs: Flow<List<Program>>
    suspend fun getProgram(name: String): Program?
    suspend fun getProgramsByUsername(username: String): List<Program>
    suspend fun insertProgram(program: Program)
    suspend fun updateProgram(program: Program)
    suspend fun removeProgram(program: Program)
}
