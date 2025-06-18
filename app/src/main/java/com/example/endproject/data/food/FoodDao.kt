package com.example.endproject.data.food

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface  FoodDao{
    //suspend 쓰는이유 -> 비동기 처리 함수 설정
    @Insert
    suspend fun insertFood(food : Food)

    @Query("SELECT * FROM Food_table WHERE name = :name LIMIT 1") //오직 한개만 반환
    suspend fun getFood(name: String) : Food?

    @Query("SELECT * FROM Food_table")
    suspend fun getAllFood() : List<Food>

    @Query("SELECT * FROM Food_table")
    fun getLiveAllFood() : LiveData<List<Food>>

    //DB에 있는 식단에 추가된 중복 데이터 아닌 것을 삭제
    @Query ("DELETE FROM food_table WHERE NAME = :name and dailyMealId IS NULL")
    suspend fun deleteCertainFood(name : String)

    @Query ("SELECT fid FROM Food_table WHERE NAME = :name")
    suspend fun getId(name : String) : Int

    //DB에 있는 것 중에 식단으로 추가 안된 경우만  -> DB에서 음식 삭제 시
    @Query("SELECT * FROM Food_table WHERE dailyMealId IS NULL")
    suspend fun getBaseFoods(): List<Food>

    //위와 같지만 프래그먼트에 뿌릴떄 -> null만
    @Query("SELECT * FROM Food_table WHERE dailyMealId IS NULL")
    fun getBaseFoodsAsLive(): LiveData<List<Food>>

    //null 이 아닌 음식을 불러오기
    @Query("SELECT * FROM food_table WHere dailyMealId is not null")
    suspend fun getTodayFoods() : List<Food>

    @Query("SELECT * FROM food_table WHere dailyMealId is not null")
    fun getTodayFoodLive() : LiveData<List<Food>>

    // 하루 식단 초기화
    @Query("DELETE FROM Food_table WHERE dailyMealId IS NOT NULL")
    suspend fun deleteTodayFoods()

    //mealid 리턴하는 함수
    @Query("SELECT COUNT(*) FROM Food_table WHERE dailyMealId IS NOT NULL")
    suspend fun returnMealIDInc() : Int

    //mealID 받으면 맞는거 지움
    @Query("DELETE FROM food_table WHERE dailyMealId = :number")
    suspend fun deleteTodayFoodById(number : Int)

}