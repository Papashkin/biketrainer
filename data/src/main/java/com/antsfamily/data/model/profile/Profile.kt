package com.antsfamily.data.model.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Profile] - data class with information about user
 * @param name - user name;
 * @param age - user age;
 * @param weight - user weight;
 * @param height - user height;
 */
@Entity
data class Profile(
    @PrimaryKey var name: String,
    var age: Int,
    var weight: Float,
    var height: Float
)
