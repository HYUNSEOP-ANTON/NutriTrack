package com.example.endproject.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.endproject.databinding.FragmentHomeBinding
import com.example.endproject.viewModel.HomeViewModel
import com.example.endproject.data.food.Food
import com.example.endproject.viewModel.FoodViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import com.example.endproject.R
import com.example.endproject.viewModel.UserViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var adaptor: HomeAdaptor

    private var userTargetCal: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[HomeViewModel::class.java]

        foodViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FoodViewModel::class.java]

        userViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adaptor = HomeAdaptor(listOf())
        binding.foodRecyclerView.adapter = adaptor
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // userLive + foodLive 연결
        userViewModel.returnUserLive().observe(viewLifecycleOwner) { user ->
            userTargetCal = user.target_calorie

            homeViewModel.getTodayFood().observe(viewLifecycleOwner) { list ->
                adaptor.updateList(list)
                updateChartAndText(list)
            }
        }

        binding.btnReset.setOnClickListener {
            lifecycleScope.launch {
                foodViewModel.clearTodayFood()
            }
        }

        binding.btnAddFood.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nav_today_add)
        }

        binding.btndeleteFood.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nav_today_delete)
        }
    }

    private fun updateChartAndText(foodList: List<Food>) {
        val kcalSum = foodList.sumOf { it.calories }
        val proteinSum = foodList.sumOf { it.protein }
        val carbSum = foodList.sumOf { it.carb }
        val fatSum = foodList.sumOf { it.fat }

        val calText = if (userTargetCal != null) {
            String.format("섭취 칼로리 : %.0f kcal / 목표 %.0f kcal", kcalSum, userTargetCal)
        } else {
            String.format("섭취 칼로리 : %.0f kcal / 목표 - kcal", kcalSum)
        }
        binding.totalCal.text = calText

        val pieChart = binding.pieChart
        if ((kcalSum + proteinSum + carbSum + fatSum) == 0.0) {
            val dummyEntries = listOf(PieEntry(1f, ""))
            val dummySet = PieDataSet(dummyEntries, "")
            dummySet.setColors(Color.LTGRAY)
            dummySet.setDrawValues(false)
            pieChart.data = PieData(dummySet)
            pieChart.centerText = "공복 상태"
            pieChart.setCenterTextSize(20f)
        } else {
            val entries = listOf(
                PieEntry(carbSum.toFloat(), "탄수화물"),
                PieEntry(proteinSum.toFloat(), "단백질"),
                PieEntry(fatSum.toFloat(), "지방")
            )
            val dataSet = PieDataSet(entries, "").apply {
                colors = listOf(
                    Color.parseColor("#FF6B6B"),
                    Color.parseColor("#1E90FF"),
                    Color.parseColor("#FFD700")
                )
                valueTextSize = 20f
            }
            pieChart.data = PieData(dataSet)
            pieChart.centerText = "총 섭취"
            pieChart.setCenterTextSize(20f)
        }

        pieChart.legend.apply {
            isEnabled = true
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
            textSize = 14f
            form = Legend.LegendForm.CIRCLE
            formSize = 14f
            xEntrySpace = 20f
            yEntrySpace = 15f
        }

        pieChart.description.isEnabled = false
        pieChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
