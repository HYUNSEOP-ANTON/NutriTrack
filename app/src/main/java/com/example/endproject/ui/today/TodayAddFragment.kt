package com.example.endproject.ui.today

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.data.food.Food
import com.example.endproject.databinding.FragmentTodayAddBinding
import com.example.endproject.viewModel.FoodViewModel
import kotlinx.coroutines.launch

class TodayAddFragment : Fragment() {

    private lateinit var binding: FragmentTodayAddBinding
    private lateinit var foodViewModel: FoodViewModel
    private var allFoodList: List<Food> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodViewModel = ViewModelProvider(requireActivity())[FoodViewModel::class.java]

        // 음식 리스트 가져와서 라디오 버튼 동적 추가
        foodViewModel.foodList.observe(viewLifecycleOwner) { foodList ->
            allFoodList = foodList
            renderFoodOptions(foodList)
        }

        // 검색창에 텍스트 입력하면 필터링
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val filteredList = allFoodList.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                renderFoodOptions(filteredList)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 추가 버튼 클릭 시 처리
        binding.btnAdd.setOnClickListener {
            val gramsText = binding.gramsInput.text.toString().trim()
            val grams = gramsText.toIntOrNull()
            val selectedId = binding.foodRadioGroup.checkedRadioButtonId

            if (selectedId == -1 || grams == null || grams <= 0) {
                Toast.makeText(requireContext(), "음식이 추가되지 않았어요!", Toast.LENGTH_SHORT).show()
                binding.searchInput.text?.clear()                 // 검색창 초기화
                binding.gramsInput.text?.clear()                  // 그램 수 초기화
                binding.foodRadioGroup.clearCheck()               // 라디오 버튼 초기화
                return@setOnClickListener
            }

            val selectedRadioButton = binding.foodRadioGroup.findViewById<RadioButton>(selectedId)
            val selectedFood = selectedRadioButton?.tag as? Food

            selectedFood?.let {
                // 등록 로직 여기에 작성
                Toast.makeText(requireContext(), "${it.name} ${grams}g 추가됐어요", Toast.LENGTH_SHORT).show()
                binding.searchInput.text?.clear()                 // 검색창 초기화
                binding.gramsInput.text?.clear()                  // 그램 수 초기화
                binding.foodRadioGroup.clearCheck()               // 라디오 버튼 초기화
                lifecycleScope.launch {
                    foodViewModel.addTodayFood(selectedFood,grams) //id는 1인 형태로 DB에 추가되었음
                }
            }

            //커서 지우는 문제
//            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            binding.searchInput.clearFocus()
            binding.gramsInput.clearFocus()
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun renderFoodOptions(foodList: List<Food>) {
        val group = binding.foodRadioGroup
        group.removeAllViews()

        foodList.forEach { food ->
            val radio = RadioButton(requireContext()).apply {
                text = food.name
                tag = food // -> 태그에 음식이 저장되었음
                textSize = 16f
            }
            group.addView(radio)
        }

        if (foodList.isNotEmpty()) {
            group.check(group.getChildAt(0).id)
        }

        binding.foodRadioGroup.clearCheck() //기본값은 아무것도 선택 안되게
    }
}
