package com.antsfamily.data.local.database

import androidx.room.*
import com.antsfamily.data.model.program.Program
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProgramDao {
    @Query("SELECT * from program")
    abstract suspend fun getAll(): List<Program>

    @Query("SELECT * from program")
    abstract fun getPrograms(): Flow<List<Program>>

    @Query("Select * from program where title = :title")
    abstract suspend fun getProgram(title: String): Program?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertProgram(profile: Program)

    @Update
    abstract suspend fun updateProgram(program: Program)

    @Delete
    abstract suspend fun deleteProgram(profile: Program)
}
