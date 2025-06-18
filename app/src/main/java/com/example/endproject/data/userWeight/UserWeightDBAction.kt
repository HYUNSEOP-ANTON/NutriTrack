package com.example.endproject.data.userWeight

import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserWeightDBAction (private val dao: UserWeightDao){

    fun getUserWeightEverything() : LiveData<List<UserWeight>>{
        return dao.getAllWeights()
    }

    suspend fun getFirstWeight(): UserWeight{
        return dao.getFirstWeight()
    }
    suspend fun addFirstUserWeight(kgs: Float) {
        val easyDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayString = easyDate.format(Date())

        val newRecord = UserWeight(
            date = todayString,
            weight = kgs
        )

        dao.addWeight(newRecord)
    }
    suspend fun checkIfready() : Boolean{
        val flag = dao.checkIfReady() == 1
        return flag
    }

    fun returnRecent() : LiveData<UserWeight>{
        return dao.getLatestWeight()
    }
    suspend fun addNewUserWeight(kgs : Float){
        val easyDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayString = easyDate.format(Date())
        val newOne = UserWeight(
            date = todayString,
            weight = kgs
        )
        return dao.addWeight(newOne)
    }
}