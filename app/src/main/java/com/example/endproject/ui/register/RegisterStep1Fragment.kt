package com.example.endproject.ui.register

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.R
import com.example.endproject.databinding.FragmentRegisterStep1Binding
import com.example.endproject.viewModel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterStep1Fragment : Fragment() {
    private lateinit var binding: FragmentRegisterStep1Binding

    //뷰모델을 가져오기 -> 레지스터액티비티 관점에서 만들어짐
    //앞으로 나올 모든 레지스터 프래그먼트에서 필요한 것
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        //종료 버튼, 뒤로가기 시
        binding.btnBack.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("앱 종료")
                .setMessage("아직 유저 등록이 완료되지 않았어요!\n종료하시겠어요?")
                .setPositiveButton("종료") { _, _ ->
                    requireActivity().finish()
                }
                .setNegativeButton("취소", null)
                .show()
        }

        binding.btnNext.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            if (name.isNotBlank() &&name.length <= 5) {
                //근데 ... value로 접근하는 것 보다 setter 만들어서 캡슐화 해보자~
                userViewModel.userNameSetter(name)
                // 트랜지션 효과 적용
                val welcomeText = "$name 님,\n만나서 반가워요! ✨"
                binding.nameWelcome.text = welcomeText
                // 애니메이션
                binding.nameWelcome.scaleX = 0.8f
                binding.nameWelcome.scaleY = 0.8f
                binding.nameWelcome.alpha = 0f
                binding.nameWelcome.visibility = View.VISIBLE

                binding.nameWelcome.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(1000)
                    .withEndAction {
                        lifecycleScope.launch {
                            delay(1500) // 2초 대기
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right)
                                .replace(R.id.register_fragment_container, RegisterStep2Fragment())
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                    .start()
            } else if(name.isBlank()){
                Toast.makeText(requireContext(), "이름을 알려주시겠어요?", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "5자 이내로 만 해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
