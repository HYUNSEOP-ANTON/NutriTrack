package com.example.endproject.data.food

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food_table")
data class Food(
    @PrimaryKey(autoGenerate = true) val fid: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "carbohydrates") val carb: Double,
    @ColumnInfo(name = "protein") val protein: Double,
    @ColumnInfo(name = "fat") val fat: Double,
    @ColumnInfo(name = "calories") val calories: Double,
    @ColumnInfo(name = "grams") val grams: Int,
    @ColumnInfo(name = "dailyMealId") var mealId: Int? = null
//어떤 식사에 속하는지 선택했고 -> 나중에 유저가 추가시에만 설정
    //즉 불러올때 null인것만 불러오고 -> 하루 식단에 추가 할때는 null이 아닌것만 불러옴
    //DB에는 중복데이터 있지만 관리 가능해짐
)