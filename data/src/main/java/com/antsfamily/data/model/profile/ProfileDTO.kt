package com.antsfamily.data.model.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.antsfamily.domain.model.Profile

/**
 * [ProfileDTO] - data class with information about user
 * @param name - user name;
 * @param age - user age;
 * @param weight - user weight;
 * @param height - user height;
 */
@Entity
data class ProfileDTO(
    @PrimaryKey var name: String,
    var age: Int,
    var weight: Float,
    var height: Float
)


fun ProfileDTO.toDomainModel(): Profile = Profile(name, age, weight, height)

fun Profile.toDTO(): ProfileDTO = ProfileDTO(name, age, weight, height)