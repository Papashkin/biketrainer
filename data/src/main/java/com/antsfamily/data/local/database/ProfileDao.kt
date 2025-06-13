package com.antsfamily.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.antsfamily.data.model.profile.ProfileDTO

@Dao
abstract class ProfileDao {

    @Query("SELECT * from profiledto")
    abstract suspend fun getAll(): List<ProfileDTO>

    @Query("Select * from profiledto where name = :profileName")
    abstract suspend fun getProfile(profileName: String): ProfileDTO?

    @Insert
    abstract suspend fun addProfile(profile: ProfileDTO)

    @Update
    abstract suspend fun updateProfile(profile: ProfileDTO)

    @Delete
    abstract suspend fun deleteProfile(profile: ProfileDTO)
}
