package com.example.endproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.endproject.data.food.Food
import com.example.endproject.data.food.FoodDao
import com.example.endproject.data.user.User
import com.example.endproject.data.user.UserDao
import com.example.endproject.data.userWeight.UserWeight
import com.example.endproject.data.userWeight.UserWeightDao

@Database(entities = [Food::class, User::class, UserWeight::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun foodDao() : FoodDao
    abstract fun userDao() : UserDao
    abstract fun userWeightDao() : UserWeightDao


    //데이터 베이스를 하나만 만들거니간 static해야하는데...
    //앱 데이터 베이스는 애초에 앱 데이터베이스 상속이 필요
    //그래서 컴패니언 오브젝트 사용
    companion object {
        var db : AppDatabase? = null

        //먼저 db가 널인지 판단하고
        //아직 안만들어졌으면 리턴
        fun getDataBase(context: Context): AppDatabase {
            if(db == null){
                db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "App_database" //앱데이터 베이스로 받기
                ).fallbackToDestructiveMigration().build()
            }
            return db!!
        }
    }
}