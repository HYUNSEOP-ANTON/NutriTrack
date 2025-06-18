package com.example.endproject.data.user

import androidx.lifecycle.LiveData

class UserDBAction(private val dao: UserDao){

    //유저가 DB에 등록되었는지 확인 함수 -> 초기 실행
    //만약 유저 없으면 유저 등록 창으로 변환 할 수 있도록 설정
    suspend fun isUserHere() : Int{
        return dao.isUserHere()
    }

    //유저가 DB에 있다면 -> 유저 반환 단순 조회용
    suspend fun returnUser() : User{
        return dao.returnMe()
    }
    //위와 같지만 라이브로
    fun returnUserLive() : LiveData<User>{
        return dao.returnMeLive()
    }

    //유저 추가 하는 것
    suspend fun addUser(name: String, sex: Int, age: Int, height: Float,
                        weight: Float, targetWeight: Float, week: Int, activity_level : Float)
    {
        val user = User(name = name,sex = sex,age = age, height = height,
            weight = weight,BMI = 0f, BMR = 0f, target_weight = targetWeight, activity_level = activity_level,
            TDEE = 0f, week = week, target_calorie = 0f,  days = 0
        )
        dao.addUser(user)
    }

    suspend fun updateCalculations() {
        val user = dao.returnMe() //지금 저장된 유저 정보 가져와서
        calculateBMI(user) //BMI 계산
        calculateBMR(user) //BMR 계산-> 기초대사량
        calculateTDEE(user) //총 하루 소비 칼로리 TDEE 계산
        calculateTargetCalorie(user) // 하루 대비 칼로리 계산 후
        calculateEndDay(user) //몇 일 남았는지 기록

        //도움말로 -> 아침 공복 체중을 재주세요 ->
        //그날 몸무게 데이터에 따른 모든 것을 계산 후
        //하루 칼로리 및 모든 정보 출력 형태

        dao.updateUser(user)
    }

    //BMI 계산 부분
    fun calculateBMI(user: User){
        var heightInMeter = user.height / 100
        user.BMI = user.weight / (heightInMeter * heightInMeter)
    }
    fun calculateBMR(user: User){
        user.BMR = if (user.sex ==1){
            10 * user.weight + 6.25f * user.height - 5 * user.age + 5
        }
        else{
            10 * user.weight+ 6.25f * user.height - 5 * user.age - 161
        }
    }
    //TDEE
    fun calculateTDEE(user : User){
        user.TDEE = user.BMR * user.activity_level
    }
    //목표 칼로리 만들어주는 함수
    fun calculateTargetCalorie(user : User){
        user.target_calorie =
            user.TDEE - ((user.weight - user.target_weight) * 7700) / (user.week * 7)
    }
    //목표까지 몇 일 남았는 함수 (int)
    fun calculateEndDay(user: User) : Int{
        return (user.week * 7) - user.days
    }
    //오늘 체중 들어오면 다시 계산
    suspend fun addTodayWeightUpdate(kgs : Float){
        return dao.updateUserWeight(kgs)
    }
}