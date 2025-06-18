package com.example.endproject.data.userWeight

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserWeightDao {
    //먼저 유저 데이터를 다 반환 -> 나중에 id로 정렬하면됌 라이브로 리턴
    @Query("SELECT * FROM user_weights WHERE userId = 1")
    fun getAllWeights(): LiveData<List<UserWeight>>

    @Insert
    suspend fun addWeight(newWeight : UserWeight)

    //가장 최근 몸무게 보여주기 라이브
    @Query("SELECT * FROM user_weights WHERE userId = 1 ORDER BY id DESC LIMIT 1")
    fun getLatestWeight(): LiveData<UserWeight>

    //가장 처음 몸무게 보여주기 라이브 -> 프래그먼트 사용
    @Query("SELECT * FROM user_weights WHERE userId = 1 ORDER BY id ASC LIMIT 1")
    fun getFirstWeightLive(): LiveData<UserWeight>

    //가장 처음 몸무게 보여주기 일반 -> 메인액티비티 사용
    @Query("SELECT * FROM user_weights WHERE userId = 1 ORDER BY id ASC LIMIT 1")
    suspend fun getFirstWeight(): UserWeight

    @Query("SELECT COUNT(*) FROM user_weights")
    suspend fun checkIfReady() : Int

}