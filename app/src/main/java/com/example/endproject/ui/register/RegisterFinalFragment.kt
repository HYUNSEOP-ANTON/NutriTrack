package com.example.endproject.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.MainActivity
import com.example.endproject.data.AppDatabase
import com.example.endproject.data.food.FoodDBAction
import com.example.endproject.databinding.FragmentRegisterFinalBinding
import com.example.endproject.viewModel.UserViewModel
import com.example.endproject.viewModel.UserWeightViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterFinalFragment : Fragment() {
    private lateinit var userViewModel : UserViewModel
    private lateinit var binding : FragmentRegisterFinalBinding

    private lateinit var userWeightViewModel: UserWeightViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        userWeightViewModel = ViewModelProvider(requireActivity())[UserWeightViewModel::class.java]


        val db =AppDatabase.getDataBase(requireActivity())

        //음식
        val foodDao = db.foodDao()
        FoodDBAction(foodDao)

        //로딩되면서...
        lifecycleScope.launch{
            userViewModel.addUserToDBActionWithInfoRegister()
            userViewModel.logPrintTestEnd() //태그 :userViewModel
            userWeightViewModel.addUserWeightToDB() // 체중 정보 저장됨
            userViewModel.refreshUserData()

            Log.d("bug1", "onViewCreated: ${userWeightViewModel.checkIfready()}")
            if(userWeightViewModel.checkIfready()){
                delay(1500)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}