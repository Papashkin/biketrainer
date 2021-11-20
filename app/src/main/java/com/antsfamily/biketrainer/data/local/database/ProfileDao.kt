package com.antsfamily.biketrainer.data.local.database

import androidx.room.*
import com.antsfamily.biketrainer.data.models.profile.Profile
import com.antsfamily.biketrainer.data.models.profile.ProfileWithPrograms
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProfileDao {

    @Query("SELECT * from profile")
    abstract suspend fun getAll(): List<Profile>

    @Query("Select * from profile where name = :profileName")
    abstract suspend fun getProfile(profileName: String): Profile?

    @Transaction
    @Query("SELECT * from profile where name = :profileName")
    abstract fun getProfileFlow(profileName: String): Flow<ProfileWithPrograms>

    @Insert
    abstract suspend fun addProfile(profile: Profile)

    @Update
    abstract suspend fun updateProfile(profile: Profile)

    @Delete
    abstract suspend fun deleteProfile(profile: Profile)
}
