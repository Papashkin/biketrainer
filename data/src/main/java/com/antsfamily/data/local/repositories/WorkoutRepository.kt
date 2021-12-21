package com.antsfamily.data.local.repositories

import com.antsfamily.data.model.program.Program
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun getAllPrograms(): List<Program>
    val programs: Flow<List<Program>>
    suspend fun getProgram(name: String): Program?
    suspend fun getProgramsByUsername(username: String): List<Program>
    suspend fun insertProgram(program: Program)
    suspend fun updateProgram(program: Program)
    suspend fun removeProgram(program: Program)
}
