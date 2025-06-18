package com.example.endproject.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.endproject.data.AppDatabase
import com.example.endproject.data.food.Food
import com.example.endproject.data.food.FoodDBAction

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val fdao = AppDatabase.getDataBase(application).foodDao()
    private val dbAction = FoodDBAction(fdao)
    var foodList : LiveData<List<Food>> = dbAction.returnLiveDataFoodList()

    suspend fun getNotLiveFoodList() : List<Food>{
        return dbAction.returnFoodList()
    }
    //읍식 받고 (프래그먼트에서 코루틴으로 써야함)
    suspend fun addFoodInDB(newFood : Food){
        dbAction.addFood(newFood)
    }
    suspend fun deleteFoodInDB(nameOfFood : String){
        dbAction.deleteFood(nameOfFood)
    }

    //DB에서 삭제하는 음식 선택 시 null 빼고 출력
    suspend fun getOnlyBaseFoodList(): List<Food> {
        return dbAction.getBaseFoodList()
    }

    //홈 프래그먼트에서 음식 넣는 섹션 !!!
    //그냥 mealID 1로 해서 넣는 방법 채택
    suspend fun addTodayFood(food : Food, grams : Int){
        //읍식과 그램수를 받는데
        dbAction.addTodayFood(food,grams)
    }

    //오늘 먹은거 다 삭제
    suspend fun clearTodayFood(){
        return dbAction.clearTodayFood()
    }
    //오늘 특정 음식만 삭제
    suspend fun clearTodayFoodCertainID(number : Int){
        dbAction.deleteOneTodayFood(number)
    }
    //오늘 먹은 음식만 리턴
    suspend fun getTodayEatenFood(): List<Food>{
        return dbAction.getTodayFoodListNotLive()
    }

    fun getTodayEatenFoodLive () : LiveData<List<Food>>{
        return dbAction.getTodayFoodList()
    }
}