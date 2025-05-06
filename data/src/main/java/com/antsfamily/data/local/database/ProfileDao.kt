package com.antsfamily.data.local.database

import androidx.room.*
import com.antsfamily.data.model.profile.Profile

@Dao
abstract class ProfileDao {

    @Query("SELECT * from profile")
    abstract suspend fun getAll(): List<Profile>

    @Query("Select * from profile where name = :profileName")
    abstract suspend fun getProfile(profileName: String): Profile?

    @Insert
    abstract suspend fun addProfile(profile: Profile)

    @Update
    abstract suspend fun updateProfile(profile: Profile)

    @Delete
    abstract suspend fun deleteProfile(profile: Profile)
}
