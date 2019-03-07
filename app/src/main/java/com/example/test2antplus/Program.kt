package com.example.test2antplus

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject

/**
 * [Program] - data set class for trainings program
 * @param name - name of program
 * @param program - text representation of the program values (<time>*<power>|<time>*<power>|...)
 */
@Entity
class Program @Inject constructor(
    @PrimaryKey(autoGenerate = true)
    private var id: Int,

    @ColumnInfo(name = "name")
    private var name: String,

    @ColumnInfo(name = "program")
    private var program: String
) {
    fun getId() = this.id
    fun getName() = this.name
    fun getProgram() = this.program

    fun setName(newName: String) {
        this.name = newName
    }

    fun setProgram(newProgram: String) {
        this.program = newProgram
    }
}