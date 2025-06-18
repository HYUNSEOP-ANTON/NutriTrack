package com.example.endproject.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.data.food.Food
import com.example.endproject.databinding.FragmentTodayDeleteBinding
import com.example.endproject.viewModel.FoodViewModel
import kotlinx.coroutines.launch
import android.widget.Toast

class TodayDeleteFragment: Fragment() {

    private lateinit var binding: FragmentTodayDeleteBinding
    private lateinit var foodViewModel : FoodViewModel
    private var todayFoodList: List<Food> = listOf()
    private lateinit var todayFoodListLive: LiveData<List<Food>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayDeleteBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodViewModel = ViewModelProvider(requireActivity())[FoodViewModel::class.java]

        //음식 리스트 가져와서 다중 선택 버튼으로 보여주기
        // step1 . 일단 비동기로 받으시오
//        lifecycleScope.launch{
//            todayFoodList = foodViewModel.getTodayEatenFood();
//            todayFoodList = todayFoodList.sortedBy { it.mealId }; //먼저 정렬 해주고 ~~
//            renderFoodsEaten(todayFoodList);
//        }
        //라이브로 처리하기
        todayFoodListLive = foodViewModel.getTodayEatenFoodLive()
        todayFoodListLive.observe(viewLifecycleOwner) {
            list ->
            val sortedList = list.sortedBy { it.mealId }
            renderFoodsEaten(sortedList)
        }

        binding.btnTodayDelete.setOnClickListener {

            val checkedMealIds = mutableListOf<Int>()

            //해당 레이아웃에 가지고 잇는 뷰 개수 까지 가면서
            for ( i in 0 until binding.checkBoxContainer.childCount){
                val counterFood = binding.checkBoxContainer.getChildAt(i) //0번 부터
                if (counterFood is CheckBox && counterFood.isChecked){
                    checkedMealIds.add(counterFood.tag.toString().toInt()) //그 인덱스의 태그를 받아서 넣으세용
                }
            }

            //DB에 반영
            lifecycleScope.launch{
                checkedMealIds.forEach{
                    it -> foodViewModel.clearTodayFoodCertainID(it)
                }
                Toast.makeText(requireContext(),"삭제가 완료되었어요!",Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    //todo -> 데이터를 뿌리고, 거기서 선택된거 반환
    private fun renderFoodsEaten(foodList:List<Food>){
        val container = binding.checkBoxContainer
        container.removeAllViews() // ← 이 줄 추가! -> 다 지움
        foodList.forEach { it ->
            val checkBox = CheckBox(requireContext()).apply{
                text = "${it.mealId}번 음식: ${it.name} / ${it.grams} g"
                tag = it.mealId
            }
            container.addView(checkBox)
        }
    }
}