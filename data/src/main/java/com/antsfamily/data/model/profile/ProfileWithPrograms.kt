package com.antsfamily.data.model.profile

import androidx.room.Embedded
import androidx.room.Relation
import com.antsfamily.data.model.program.Program

data class ProfileWithPrograms(
    @Embedded val profile: Profile,
    @Relation(entityColumn = "username", parentColumn = "name", entity = Program::class)
    val programs: List<Program> = emptyList()
)
