package com.example.endproject.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.endproject.R
import com.example.endproject.databinding.FragmentRegisterStep3Binding
import com.example.endproject.viewModel.UserViewModel
import com.example.endproject.viewModel.UserWeightViewModel

class RegisterStep3Fragment : Fragment() {
    private lateinit var binding : FragmentRegisterStep3Binding
    private lateinit var userViewModel : UserViewModel
    private lateinit var userWeightViewModel: UserWeightViewModel

    private var userHeight : Float = 0f
    private var userWeight : Float = 0f
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        userWeightViewModel = ViewModelProvider(requireActivity())[UserWeightViewModel::class.java]
        binding.btnNext.setOnClickListener {

            if (binding.editTextHeight.text.toString().isNotBlank() && binding.editTextWeight.text.toString().isNotBlank()){
                //뷰모델에 저장
                userHeight = binding.editTextHeight.text.toString().toFloat()
                userWeight = binding.editTextWeight.text.toString().toFloat()

                userViewModel.userHeightSetter(userHeight)
                userViewModel.userWeightSetter(userWeight)
                userWeightViewModel.userFirstWeightSetter(userWeight) //체중 기록 DB에 저장
                //트랜지션
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right)
                    .replace(R.id.register_fragment_container, RegisterStep4Fragment())
                    .addToBackStack(null)
                    .commit()
            }
            else{
                Toast.makeText(requireContext(),"다시 한번 체크해주세요!",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


    }
}