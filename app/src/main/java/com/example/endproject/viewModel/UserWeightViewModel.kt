package com.example.endproject.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.endproject.data.AppDatabase
import com.example.endproject.data.userWeight.UserWeight
import com.example.endproject.data.userWeight.UserWeightDBAction

class UserWeightViewModel(application: Application) : AndroidViewModel(application)
{
    private val dao = AppDatabase.getDataBase(application).userWeightDao()
    private val dbActon : UserWeightDBAction = UserWeightDBAction(dao)

    //체중 프래그 먼트 (유저에서 보여질 것)
    val userWeightLive : LiveData<List<UserWeight>> = dbActon.getUserWeightEverything()

    //첫 회원가입 시 것만 미리 저장
    private val _userFirstWeight = MutableLiveData<Float>()
    val userFirstWeight : LiveData<Float> get() = _userFirstWeight

    //레지스터 setter
    fun userFirstWeightSetter(pound : Float){
        _userFirstWeight.value = pound
    }

    //가장 처음 몸무게 기준으로 데이터베이스에 추가
    suspend fun addUserWeightToDB(){
        dbActon.addFirstUserWeight(this._userFirstWeight.value!!)
    }

    //이미 저장되었으면 리턴 true
    suspend fun checkIfready() : Boolean{
        return dbActon.checkIfready()
    }

    //가장 최근 것 리턴
    fun returnRecently() : Int{
        val ad = dbActon.returnRecent().value!!.id
        return ad
    }
    //몸무게 받은거 기준으로 데이터 저장
    suspend fun addNewWeight(kgs : Float){
        return dbActon.addNewUserWeight(kgs)
    }
}