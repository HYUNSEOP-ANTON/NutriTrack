package com.example.endproject.data.food

import androidx.lifecycle.LiveData

class FoodDBAction(private val dao: FoodDao) {
    private var listOfFood =mutableListOf<Food>(
        Food(0, "닭 가슴살", 0.0, 23.1, 2.6, 110.0, 100),
        Food(0, "닭 안심살", 0.0, 22.5, 1.0, 105.0, 100),
        Food(0, "돼지고기 등심", 0.0, 20.9, 9.4, 183.0, 100),
        Food(0, "돼지고기 목살", 0.0, 16.5, 21.0, 267.0, 100),
        Food(0, "소고기 등심", 0.0, 19.0, 17.0, 250.0, 100),
        Food(0, "연어", 0.0, 20.0, 13.0, 208.0, 100),
        Food(0, "참치 통조림", 0.0, 25.5, 8.0, 200.0, 100),
        Food(0, "고등어", 0.0, 20.0, 12.0, 210.0, 100),
        Food(0, "현미밥", 23.5, 2.4, 1.0, 111.0, 100),
        Food(0, "흰쌀밥", 28.6, 2.5, 0.3, 130.0, 100),
        Food(0, "오트밀", 66.3, 16.9, 6.9, 389.0, 100),
        Food(0, "고구마", 27.9, 1.6, 0.1, 119.0, 100),
        Food(0, "감자", 17.5, 2.0, 0.1, 77.0, 100),
        Food(0, "사과", 13.8, 0.3, 0.2, 52.0, 100),
        Food(0, "계란", 1.1, 12.6, 10.6, 143.0, 100),
        Food(0, "우유", 4.8, 3.3, 3.6, 64.0, 100),
        Food(0, "저지방 우유", 5.0, 3.6, 1.0, 47.0, 100),
        Food(0, "두부", 1.9, 8.2, 4.8, 94.0, 100),
        Food(0, "그릭 요거트", 3.6, 10.0, 0.4, 59.0, 100),
        Food(0, "치즈", 1.3, 25.0, 33.0, 402.0, 100),
        Food(0, "베이컨", 1.4, 12.0, 42.0, 458.0, 100),
        Food(0, "마이프로틴 WPI", 6.7, 76.7, 5.0, 363.0, 100),
        Food(0, "흰 식빵", 48.0, 8.0, 3.0, 265.0, 100),
        Food(0, "통밀 식빵", 40.0, 12.0, 3.0, 247.0, 100),
        Food(0, "퀴노아", 21.3, 4.4, 1.9, 120.0, 100),
        Food(0, "파스타면", 75.0, 13.0, 1.5, 371.0, 100),
        Food(0, "아몬드", 21.6, 21.2, 49.9, 579.0, 100),
        Food(0, "호두", 13.7, 15.2, 65.2, 654.0, 100),
        Food(0, "땅콩", 16.1, 25.8, 49.2, 567.0, 100),
        Food(0, "김치", 2.4, 1.6, 0.3, 20.0, 100),
        Food(0, "오이", 3.6, 0.6, 0.1, 15.0, 100),
        Food(0, "배추", 1.8, 1.1, 0.2, 13.0, 100),
        Food(0, "블루베리", 14.5, 0.7, 0.3, 57.0, 100),
        Food(0, "딸기", 7.7, 0.8, 0.3, 32.0, 100),
        Food(0, "아보카도", 8.5, 2.0, 15.0, 160.0, 100),
        Food(0, "샐러드 채소 믹스", 3.0, 1.5, 0.2, 20.0, 100),
        Food(0, "곤약", 0.1, 0.1, 0.1, 5.0, 100),
        Food(0, "브로콜리", 6.6, 2.8, 0.4, 34.0, 100),
        Food(0, "양배추", 5.8, 1.3, 0.1, 25.0, 100),
        Food(0, "버섯", 3.3, 2.2, 0.1, 22.0, 100)
    )
    //중복 체크 해서 없는 음식만 DB에 추가
    //왜냐하면 기존에 있는 음식에 대해 계속 추가하면 중복되는 인스턴스만 증가
    suspend fun dbLoad() {
        val existingNames = dao.getBaseFoods().map { it.name }.toSet()
        for (newFood in listOfFood) {
            if (newFood.name !in existingNames) {
                dao.insertFood(newFood)
            }
        }
    }
    //DB에 음식 추가
     suspend fun addFood(newFood: Food) {
         this.dao.insertFood(newFood)
    }
    //DB에 있는 음식 삭제
    suspend fun deleteFood(name : String){
        this.dao.deleteCertainFood(name)
    }

     //출력 테스트용
    suspend fun returnFoodList() : List<Food>{
         return dao.getAllFood()
     }
     //라이브 데이터 출력용
     fun returnLiveDataFoodList() : LiveData<List<Food>>{
         val allFoods = dao.getBaseFoodsAsLive()
         return allFoods
     }

    //DB에 삭제할 음식으로 뿌리는 경우 -> null인것은 제외해서
    suspend fun getBaseFoodList() : List<Food>{
        return dao.getBaseFoods()
    }

    //오늘 음식 라이브 데이터 뿌리기
    fun getTodayFoodList() : LiveData<List<Food>>{
        return dao.getTodayFoodLive()
    }
    //오늘 먹은 거 라이브로 안뿌리기
    suspend fun getTodayFoodListNotLive() : List<Food>{
        return dao.getTodayFoods()
    }
    //오늘 음식 먹는 DB에 재 추가 하는 함수
    suspend fun addTodayFood(food:Food, grams:Int){
        val ratio = grams / 100.0
        val mealID : Int = dao.returnMealIDInc() + 1 //처음에 0이니깐... 1로 늘려요
        val newFood = Food(
            fid =0,
            name = food.name,
            carb = food.carb * ratio,
            protein = food.protein * ratio,
            fat = food.fat * ratio,
            calories = food.calories * ratio,
            grams = grams,
            mealId = mealID
        )
        dao.insertFood(newFood)
    }
    //오늘 먹은 음식 다 지우기
    suspend fun clearTodayFood(){
        dao.deleteTodayFoods()
    }
    //오늘 먹은 것 중에서 특정 음식 지우기
    //푸드 모델에서 선택한 음식을 ID 리턴해서 지우는 방법 채택
    suspend fun deleteOneTodayFood(number : Int){
        dao.deleteTodayFoodById(number)
    }

}