package com.example.endproject.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//유저 정보
@Entity(tableName = "User_table") //데이터 베이스에서 쓰는 테이블로 사용하거나 일반 클래스처럼 사용 가능
data class User(
    //생성자 동시에 속성 만들기
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "name") val name: String, //처음 입력 시 ㅇ
    @ColumnInfo(name = "sex") val sex: Int, //1남자 2여자 //처음 입력 시 ㅇ
    @ColumnInfo(name = "age") var age:Int, //처음 입력 시 ㅇ
    @ColumnInfo(name = "height") val height: Float, //처음 입력 시 ㅇ
    @ColumnInfo(name = "weight") var weight: Float, //처음 입력 시 ㅇ
    @ColumnInfo(name = "BMI") var BMI : Float,
    @ColumnInfo(name = "BMR") var BMR : Float,
    @ColumnInfo(name = "targetWeight") var target_weight: Float, //처음 입력 시 step 5
    @ColumnInfo(name = "activityLevel") var activity_level: Float, //step 4에서 받자
    @ColumnInfo(name = "TDEE") var TDEE : Float,
    @ColumnInfo(name = "targetDate") var week: Int, //처음 입력 시 step 5
    @ColumnInfo(name = "targetCalorie") var target_calorie: Float,
    @ColumnInfo(name = "days") var days : Int =0
)
//첫 입력 시, 이름, 나이, 성별, 키, 몸무게, 목표 체중, 목표 주를 받을 예정 -> 활동 개수 포함