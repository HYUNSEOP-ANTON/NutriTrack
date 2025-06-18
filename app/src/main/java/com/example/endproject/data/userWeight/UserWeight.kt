package com.example.endproject.data.userWeight

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_weights")
data class UserWeight(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //실제 저장시 1부텉 됨
    val userId: Int = 1,
    val date: String, //날짜 데이터
    val weight: Float,
)

