package com.example.endproject.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.endproject.databinding.ActivityRegisterBinding
import com.example.endproject.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //로직 수행
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //일단 fragment로 넘김 (첫 실행)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.register_fragment_container, RegisterStep1Fragment())
                .commit()
        }

    }
}

