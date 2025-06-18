package com.example.endproject.data.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    suspend fun addUser(user: User)

    @Query("SELECT * FROM User_table WHERE uid== 1")
    suspend fun returnMe() : User

    @Query("SELECT * FROM User_table WHERE uid== 1")
    fun returnMeLive() : LiveData<User>

    @Query("SELECT COUNT(*) FROM User_table WHERE uid == 1")
    suspend fun isUserHere(): Int

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE User_table SET weight = :weight WHERE uid == 1")
    suspend fun updateUserWeight(weight: Float)
}