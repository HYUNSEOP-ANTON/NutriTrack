package com.example.endproject.ui.user

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.data.user.User
import com.example.endproject.data.userWeight.UserWeight
import com.example.endproject.databinding.DialogInputWeightBinding
import com.example.endproject.databinding.FragmentUserBinding
import com.example.endproject.viewModel.FoodViewModel
import com.example.endproject.viewModel.UserViewModel
import com.example.endproject.viewModel.UserWeightViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class UserFragment : Fragment(){
    //_biding real 객체 -> binding 변수 느낌으로 접근하기위해서 사용
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var userWeightViewModel: UserWeightViewModel
    private lateinit var foodViewModel: FoodViewModel

    private var today = 0
    private var goalDay = 0

    //완료
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //뷰모델 연결
        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[UserViewModel::class.java]

        userWeightViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[UserWeightViewModel::class.java]

        foodViewModel = ViewModelProvider(
                this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FoodViewModel::class.java]

    }

    //일단 xml만 가져오세요 -> 완료
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false) //바인딩 객체 생성
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.loadUser() //처음 로딩 -> 이후는 그냥 live데이터라 계속 보여지면 될 듯

        //개발 중에 아직 유저 정보를 업데이트하기 전이라 일단 넣었음
        //추후에는 register 부분에서 다 값 받고 -> 계산 식으로 하면될듯
        userViewModel.refreshUserData()

        userViewModel.user.observe (viewLifecycleOwner){ user ->
            showUserInfos(user)
            goalDay = user.week * 7
            updateDays()
        }

        userWeightViewModel.userWeightLive.observe (viewLifecycleOwner) { it ->
            showWeightChart(it)
            today = it.last().id
            updateDays()
        }

        binding.morningWeight.setOnClickListener {
            val dialogBinding = DialogInputWeightBinding.inflate(layoutInflater)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogBinding.root)
                .setPositiveButton("확인", null) // 버튼은 아래에서 커스텀 처리
                .setNegativeButton("취소", null)
                .create()

            dialog.setOnShowListener {
                val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                button.setOnClickListener {
                    val input = dialogBinding.inputWeightToday.text.toString()
                    if (input.isNotEmpty()) {
                        val weight = input.toFloatOrNull()
                        if (weight != null) {
                            lifecycleScope.launch {
                                userWeightViewModel.addNewWeight(weight)
                                userViewModel.todayUpdate(weight)
                                foodViewModel.clearTodayFood()
                                dialog.dismiss()
                                Toast.makeText(requireContext(),"오늘 체중 기준으로 \n칼로리가 조정되었어요!",Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "숫자 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "체중이 입력되지 않았어요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            dialog.show()
        }
    }
    private fun showUserInfos(user : User){
        binding.userInfoName.text = "이름: ${user.name}"
        binding.userInfoSexAndAge.text = "성별: ${if (user.sex == 1) "남자" else "여자"} / 나이: ${user.age}세"
        binding.userCurrentWeight.text = "현재 체중 : ${user.weight} kg"
        binding.userTargetWeight.text = "목표 체중 : ${user.target_weight} kg"
        binding.userBmr.text = "기초대사량 : ${user.BMR.toInt()} kcal"
        binding.userTdee.text = "TDEE: ${user.TDEE.toInt()} kcal"
        binding.userTagetCalories.text = "목표 하루 칼로리 : ${user.target_calorie.toInt()} kcal"
    }

    //todo 차트 모양 바꾸기, 하루 아침에 공복 체중 입력기 만들기
    private fun showWeightChart(userWeight : List<UserWeight>){
        val entries = mutableListOf<Entry>()
        val sortedValue = userWeight.sortedBy { it.id } //id 별로 하기

        if(sortedValue.isNotEmpty()){
            for (i in sortedValue.indices){
                entries.add(Entry(i.toFloat(), sortedValue[i].weight))
            }

            val dataset = LineDataSet(entries, "체중 변화 그래프")
            dataset.apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                circleRadius = 4f
                lineWidth = 2f
                setCircleColor(Color.RED)
                setDrawValues(false)
            }

            binding.weightGraph.apply {
                data = LineData(dataset)
                legend.isEnabled = false

                xAxis.apply {
                    isEnabled = false
                }

                axisLeft.setDrawGridLines(false)
                axisRight.isEnabled = false
                description.isEnabled = false
                setDrawBorders(false)
                setDrawGridBackground(false)

                setExtraOffsets(10f, 30f, 10f, 30f) // 하단 여백 충분히 줌
                invalidate()
            }
        }

    }
    private fun updateDays(){
        if(today!=0 && goalDay != 0){
            binding.userDayCount.text = "${goalDay}일 중 ${today}일 째!!"
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}