package com.example.endproject.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.endproject.data.food.Food
import com.example.endproject.data.AppDatabase
import com.example.endproject.data.food.FoodDBAction

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val foodDao = AppDatabase.getDataBase(application).foodDao()
    private val foodDbAction = FoodDBAction(foodDao)

    //오늘 먹은 음식 정보 가져옴
    val todayFoodList: LiveData<List<Food>> = foodDao.getTodayFoodLive()

    suspend fun addFoodToToday(newFood: Food) {
        foodDbAction.addFood(newFood)
    }

    suspend fun getBaseFoods(): List<Food> {
        return foodDbAction.getBaseFoodList()
    }

    suspend fun resetTodayFood() {
        foodDao.deleteTodayFoods()
    }

    fun getTodayFood() : LiveData<List<Food>>{
       return  foodDbAction.getTodayFoodList()
    }
}
