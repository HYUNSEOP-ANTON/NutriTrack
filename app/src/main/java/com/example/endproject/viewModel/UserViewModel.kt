package com.example.endproject.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.endproject.data.AppDatabase
import com.example.endproject.data.user.User
import com.example.endproject.data.user.UserDBAction
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application){

    private val udao = AppDatabase.getDataBase(application).userDao()
    private val dbAction : UserDBAction = UserDBAction(udao)

    //유저 프래그먼트용
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    //레지스터 용 _변수는 진짜 접근 일반 변수는 읽기 전용 창
    //_변수 -> 진짜 바꿔야 할경우, 일반 변수 -> 밖에 보여주기용
    //이름
    private val _userName = MutableLiveData<String>()
    val userName : LiveData<String> get() = _userName

    //성별
    private val _userSex = MutableLiveData<Int>()
    val userSex : LiveData<Int> get() = _userSex

    //나이
    private val _userAge = MutableLiveData<Int>()
    val userAge : LiveData<Int> get() = _userAge

    //키
    private val _userHeight = MutableLiveData<Float>()
    val userHeight : LiveData<Float> get() = _userHeight

    //몸무게
    private val _userWeight = MutableLiveData<Float>()
    val userWeight : LiveData<Float> get() = _userWeight

    //목표 체중
    private val _userTargetWeight = MutableLiveData<Float>()
    val userTargetWeight : LiveData<Float> get() = _userTargetWeight

    //목표 주
    private val _userTargetWeek = MutableLiveData<Int>()
    val userTargetWeek : LiveData<Int> get() = _userTargetWeek

    //활동 계수
    private val _userActivityLevel = MutableLiveData<Float>()
    val userActivityLevel : LiveData<Float> get() = _userActivityLevel

    //레지스터 setter section
    // 이름
    fun userNameSetter(name: String) {
        _userName.value = name
    }
    // 성별
    fun userSexSetter(sex: Int) {
        _userSex.value = sex
    }
    // 나이
    fun userAgeSetter(age: Int) {
        _userAge.value = age
    }
    // 키
    fun userHeightSetter(height: Float) {
        _userHeight.value = height
    }

    // 몸무게
    fun userWeightSetter(weight: Float) {
        _userWeight.value = weight
    }
    // 목표 체중
    fun userTargetWeightSetter(targetWeight: Float) {
        _userTargetWeight.value = targetWeight
    }
    // 목표 주
    fun userTargetWeekSetter(week: Int) {
        _userTargetWeek.value = week
    }
    //활동 계수
    fun userActivityLevelSetter(level : Float){
        _userActivityLevel.value = level
    }

    //파이널 레지스터에서 저장하는 것
    //즉 이제 파이널 레즤스터에서 다음 함수 바로 실행해서 -> 로딩 후 홈화면 연계
    suspend fun addUserToDBActionWithInfoRegister(){
        dbAction.addUser(
            name = _userName.value!!,
            sex = _userSex.value!!,
            age = _userAge.value!!,
            height = _userHeight.value!!,
            weight = _userWeight.value!!,
            targetWeight = _userTargetWeight.value!!,
            week = _userTargetWeek.value!!,
            activity_level = _userActivityLevel.value!!,
        )
    }

    //로그 출력용
    fun logPrintTestEnd(){
        Log.d("userViewModel", "" +
                "${userName.value},${userSex.value},${userAge.value},${userHeight.value},${userWeight.value}," +
                "${userTargetWeight.value},${userTargetWeek.value},${userActivityLevel.value}")

    }

    //현재 DB에 저장된 유저 리턴하는 함수 (dao -> dbaction -> 뷰모델)
    //다만 가져오려면 dbACtion에서 업데이트 필수
    suspend fun returnUser() : User{
        return dbAction.returnUser()
    }
    //위와 같지만 라이브로
    fun returnUserLive() : LiveData<User>{
        return dbAction.returnUserLive()
    }


    //프래그먼트 전달용
    fun loadUser() {
        viewModelScope.launch {
            val userData = returnUser()
            _user.value = userData
        }
    }

    fun refreshUserData() {
        viewModelScope.launch {
            dbAction.updateCalculations() //현 정보를 가지고 계산해 ->
            loadUser() // 그리고 유저 반환해 -> 이제 프래그먼트로 넘기면 되겠죠
        }
    }

    //오늘 체중 들어오고 -> 업데이트
    suspend fun todayUpdate(kgs:Float){
        dbAction.addTodayWeightUpdate(kgs)
        refreshUserData()
    }


}