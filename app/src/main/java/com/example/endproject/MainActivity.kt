package com.example.endproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.endproject.data.AppDatabase
import com.example.endproject.data.food.FoodDBAction
import com.example.endproject.data.food.FoodDao
import com.example.endproject.data.user.UserDBAction
import com.example.endproject.data.user.UserDao
import com.example.endproject.data.userWeight.UserWeightDBAction
import com.example.endproject.data.userWeight.UserWeightDao
import com.example.endproject.databinding.ActivityMainBinding
import com.example.endproject.ui.register.RegisterActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //내비게이션 컴포넌트 연결용
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //DB 관련
    private lateinit var DB : AppDatabase
    private lateinit var FDB : FoodDBAction
    private lateinit var UDB : UserDBAction
    private lateinit var UWDB : UserWeightDBAction
    private lateinit var fdao : FoodDao
    private lateinit var udao : UserDao
    private lateinit var uwdao : UserWeightDao

    //레지스터 관련
    private var userFlag: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        //DB 세팅 먼저 -> 1순위
        DB = AppDatabase.getDataBase(this)

        //2순위
        //유저 관련 DB먼저 호출 -> 유저가 없으면 ? -> register activity 실행
        //이후 -> 유저 있으면 넘어감
        //유저 DB 생성
        udao= DB.userDao()
        UDB = UserDBAction(udao)
        lifecycleScope.launch {
            userFlag = UDB.isUserHere()
            if(userFlag == 0){
                Log.d("MainActivity", "유저 없음, 회원가입으로 갑니다")
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Log.d("MainActivity", "유저 있음, GUI go")
                initGUI()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initGUI(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바를 앱의 기본 액션바로 설정
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //여기 항상 추가해야 네비게이션에서 작동됨
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_user, R.id.nav_food, R.id.nav_tutorial
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        uwdao = DB.userWeightDao()
        fdao = DB.foodDao()
        //음식 DB 생성 및 로드 그리고 체중 데이터 가져오기
        FDB = FoodDBAction(fdao)
        UWDB = UserWeightDBAction(uwdao)
        lifecycleScope.launch{
            FDB.dbLoad()

        }

        //네비게이션 바에 이름 동적으로 바꾸기
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)  // 헤더 뷰 전체 가져오기

        val userNameText = headerView.findViewById<TextView>(R.id.navTextView1)
        val extraText = headerView.findViewById<TextView>(R.id.navTextView2)
        lifecycleScope.launch{
            userNameText.text = "${UDB.returnUser().name}님, 오늘도 화이팅!!"
            extraText.text = "시작 체중 : ${UWDB.getFirstWeight().weight}\n목표 체중 : ${UDB.returnUser().target_weight}"
        }
    }
}