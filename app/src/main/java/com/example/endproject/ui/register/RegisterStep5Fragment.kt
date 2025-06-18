package com.example.endproject.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.databinding.FragmentRegisterStep5Binding
import com.example.endproject.viewModel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterStep5Fragment : Fragment() {
    private lateinit var binding : FragmentRegisterStep5Binding
    private lateinit var userViewModel : UserViewModel

    private var targetWeight : Float = 0f
    private var targetWeek : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterStep5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.btnNext.setOnClickListener {
            if(binding.editTextTargetWeight.text.toString().isNotBlank() &&
                binding.editTextTargetWeek.text.toString().isNotBlank()){

                this.targetWeight = binding.editTextTargetWeight.text.toString().toFloat()
                this.targetWeek = binding.editTextTargetWeek.text.toString().toInt()


                //뷰모델에 저장
                userViewModel.userTargetWeightSetter(targetWeight)
                userViewModel.userTargetWeekSetter(targetWeek)

                //애니메이션
                val itsDoneText = "이제 다 끝났어요!✅"
                binding.itsDone.text=itsDoneText
                binding.itsDone.scaleX = 0.8f
                binding.itsDone.scaleY = 0.8f
                binding.itsDone.alpha = 0f
                binding.itsDone.visibility = View.VISIBLE
                binding.itsDone.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(1000)
                    .withEndAction{
                        lifecycleScope.launch {
                            delay(1500)
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    com.example.endproject.R.anim.slide_in_right,
                                    com.example.endproject.R.anim.slide_out_left,
                                    com.example.endproject.R.anim.slide_in_left,
                                    com.example.endproject.R.anim.slide_out_right
                                )
                                .replace(
                                    com.example.endproject.R.id.register_fragment_container,
                                    RegisterFinalFragment()
                                )
                                .addToBackStack(null)
                                .commit()
                    }
                    .start()

                    }
            }
            else{
                Toast.makeText(requireContext(),"목표를 입력해주세요!",Toast.LENGTH_SHORT).show()
            }
        }

        //이전 버튼 클릭 시...
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }
}