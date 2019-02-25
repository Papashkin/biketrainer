package com.example.test2antplus.data.programs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test2antplus.Program

@Database(entities = [Program::class], version = 1)
abstract class ProgramsDatabase: RoomDatabase() {
    abstract fun programDao(): ProgramDao
}