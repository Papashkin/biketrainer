package com.antsfamily.domain.model

/**
 * [Profile] - data class with information about user
 * @param name - user name;
 * @param age - user age;
 * @param weight - user weight;
 * @param height - user height;
 */
data class Profile(
    var name: String,
    var age: Int,
    var weight: Float,
    var height: Float
)
