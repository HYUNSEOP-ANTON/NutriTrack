package com.example.endproject.ui.register

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.endproject.R
import com.example.endproject.databinding.FragmentRegisterStep2Binding
import com.example.endproject.viewModel.UserViewModel

class RegisterStep2Fragment: Fragment() {
    private lateinit var binding : FragmentRegisterStep2Binding
    private lateinit var userViewModel: UserViewModel

    private lateinit var userAge: String
    private var userSex: Int = 3 //1남자 2여자 3선택 안됌

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        //뒤로 가기
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        //버튼 색 바꾸기 ->남자
        binding.btnMale.setOnClickListener {
            binding.btnMale.isSelected = true
            binding.btnFemale.isSelected = false

            binding.btnMale.setBackgroundColor(Color.parseColor("#007AFF"))
            binding.btnFemale.setBackgroundColor(Color.parseColor("#EEEEEE"))
            userSex = 1
        }

        //여자
        binding.btnFemale.setOnClickListener {
            binding.btnMale.isSelected = false
            binding.btnFemale.isSelected = true

            binding.btnFemale.setBackgroundColor(Color.parseColor("#FF69B4"))
            binding.btnMale.setBackgroundColor(Color.parseColor("#EEEEEE"))
            userSex = 2
        }

        binding.btnNext.setOnClickListener {
            if (binding.editTextAge.text.toString().isNotBlank() && userSex !=3){
                userAge = binding.editTextAge.text.toString()
                userViewModel.userSexSetter(userSex) //성별 세팅
                userViewModel.userAgeSetter(userAge.toInt()) //나이 넣고
                //트랜지션 ~
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right)
                    .replace(R.id.register_fragment_container, RegisterStep3Fragment())
                    .addToBackStack(null)
                    .commit()
            }
            else{
                Toast.makeText(requireContext(),"성별, 나이 입력 체크해주시겠어요?",Toast.LENGTH_SHORT).show()
            }

        }
    }
}