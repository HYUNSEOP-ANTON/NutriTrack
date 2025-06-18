package com.example.endproject.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.databinding.FragmentRegisterStep4Binding
import com.example.endproject.viewModel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterStep4Fragment : Fragment() {

    private lateinit var binding : FragmentRegisterStep4Binding
    private lateinit var userViewModel: UserViewModel

    private lateinit var radioGroup : RadioGroup
    private var selected : Int = -1
    private lateinit var selectedButton : RadioButton

    private var activity_level : Float = 1.0f
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //바인딩 연결
        binding = FragmentRegisterStep4Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //뷰모델 연결
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.btnNext.setOnClickListener {
            radioGroup = binding.rgLevel
            selected = radioGroup.checkedRadioButtonId //INT 형태로 받음
            if(selected != -1){ //선택 되었다면 ???
                //다만 기본 바인딩으로는 객체의 아이디를 알 수는 없음 -> 따라서 여기서는 findViewId 사용
                selectedButton =  requireView().findViewById<RadioButton>(selected)
                //선택된 버튼의 text를 검새하자...

                val str = selectedButton.tag.toString()
                    //각 맞는 활동 계수 넣고
                when (str){
                    "level1"-> {
                        this.activity_level = 1.2f
                    }
                    "level2" -> {
                        this.activity_level = 1.375f
                    }
                    "level3"-> {
                        this.activity_level = 1.55f
                    }
                    "level4" -> {
                        this.activity_level = 1.725f
                    }
                    "level5" -> {
                        this.activity_level = 1.9f
                    }
                }
                //뷰모델에 삽입 -> float로 넣었죠
                userViewModel.userActivityLevelSetter(this.activity_level)

                //애니메이션 효과
                val almostDoneText = "거의 다 왔어요 \uD83D\uDE4C"
                binding.almostDone.text=almostDoneText
                binding.almostDone.scaleX = 0.8f
                binding.almostDone.scaleY = 0.8f
                binding.almostDone.alpha = 0f
                binding.almostDone.visibility = View.VISIBLE
                binding.almostDone.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(1000)
                    .withEndAction{
                        //트랜지션
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
                                    RegisterStep5Fragment()
                                )
                                .addToBackStack(null)
                                .commit()
                        }
                    }
            }
            else{
                Toast.makeText(requireContext(),"활동 정도를 체크해주세요! ",Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

}