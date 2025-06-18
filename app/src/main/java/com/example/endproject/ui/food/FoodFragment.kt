package com.example.endproject.ui.food

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.endproject.R
import com.example.endproject.data.food.Food
import com.example.endproject.databinding.FragmentFoodBinding
import com.example.endproject.viewModel.FoodViewModel
import kotlinx.coroutines.launch


class FoodFragment : Fragment() {
    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodViewModel: FoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //Application을 받기에 이렇게 써야함... -> 지금 푸드는 home과 food 프래그먼트에서 다 쓰기에
        foodViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FoodViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FoodAdaptor(listOf())
        binding.foodDBListSection.adapter = adapter

        //뷰모델의 음식을 (람다 표현된 형태)
        //즉 뷰모델에 있는 라이브 데이터를 읽어서 이걸 실시간 UI로 반영
        foodViewModel.foodList.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }

        binding.foodDBListSection.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_food_add_delete,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showAddFoodDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_food, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.foodNameInput)
        val kcalInput = dialogView.findViewById<EditText>(R.id.kcalInput)
        val carbInput = dialogView.findViewById<EditText>(R.id.carbInput)
        val proteinInput = dialogView.findViewById<EditText>(R.id.proteinInput)
        val fatInput = dialogView.findViewById<EditText>(R.id.FatInput)
        val gramInput = dialogView.findViewById<EditText>(R.id.gramInput)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("사용자 정의 음식 추가")
            .setView(dialogView)
            .setPositiveButton("추가", null)
            .setNegativeButton("취소", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = nameInput.text.toString().trim()
            val kcalText = kcalInput.text.toString().trim()
            val carbText = carbInput.text.toString().trim()
            val proteinText = proteinInput.text.toString().trim()
            val fatText = fatInput.text.toString().trim()
            val gramText = gramInput.text.toString().trim()

            // 공백 체크 용
            if (name.isEmpty() || kcalText.isEmpty() || carbText.isEmpty()
                || proteinText.isEmpty() || fatText.isEmpty() || gramText.isEmpty()
            ) {
                Toast.makeText(requireContext(), "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //형 변환
            val kcal = kcalText.toDoubleOrNull() ?: return@setOnClickListener
            val carb = carbText.toDoubleOrNull() ?: return@setOnClickListener
            val protein = proteinText.toDoubleOrNull() ?: return@setOnClickListener
            val fat = fatText.toDoubleOrNull() ?: return@setOnClickListener
            val gram = gramText.toIntOrNull() ?: return@setOnClickListener

            var newFood : Food? = null
            if (gram == 100){
                newFood = Food(fid = 0,name,carb,protein,fat,kcal,gram)
            }
            //100그램 기준으로 저장됨
            else{
                var ratio = 100f / gram //비율 구해서
                newFood = Food(fid = 0,name, carb * ratio, protein * ratio,fat*ratio,kcal*ratio, 100)
            }

            lifecycleScope.launch {
                foodViewModel.addFoodInDB(newFood)
                Toast.makeText(requireContext(),"${newFood.name} 추가됨!",Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }
    }

    private fun showDeleteFoodDialog(foodList: List<Food>) {
        val foodNames = foodList.map { it.name }.toTypedArray()
        var selectedIndex = 0

        AlertDialog.Builder(requireContext())
            .setTitle("삭제할 음식 선택")
            .setSingleChoiceItems(foodNames, 0) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton("삭제") { _, _ ->
                val selectedName = foodNames[selectedIndex]
                lifecycleScope.launch {
                    foodViewModel.deleteFoodInDB(selectedName)
                    Toast.makeText(requireContext(), "${selectedName}이 삭제되었어요", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_add_food){
            Log.d("food","음식 추가 눌림")
            showAddFoodDialog()
        }

        else if(item.itemId == R.id.action_delete_food){
            Log.d("food","음식 삭제 눌림")
            lifecycleScope.launch {
                val list = foodViewModel.getOnlyBaseFoodList()
                showDeleteFoodDialog(list)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
