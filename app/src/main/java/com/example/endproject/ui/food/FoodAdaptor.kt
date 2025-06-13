package com.example.endproject.ui.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.endproject.data.food.Food
import com.example.endproject.databinding.FoodRecycleBinding
import com.example.endproject.R
class FoodAdaptor(private var foodList: List<Food>) :
    RecyclerView.Adapter<FoodAdaptor.FoodViewHolder>() {

    class FoodViewHolder(val binding: FoodRecycleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = FoodRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]

        holder.binding.foodName.text = food.name
        holder.binding.foodCalorie.text = String.format("%.1f", food.calories)
        holder.binding.foodCarb.text = String.format("%.1f", food.carb)
        holder.binding.foodProtein.text = String.format("%.1f", food.protein)
        holder.binding.foodFat.text = String.format("%.1f", food.fat)

        // 번갈아 배경색 적용
        val backgroundColor = if (position % 2 == 0)
            ContextCompat.getColor(holder.itemView.context, R.color.row_light_blue)
        else
            ContextCompat.getColor(holder.itemView.context, android.R.color.white)

        holder.itemView.setBackgroundColor(backgroundColor)
    }

    override fun getItemCount(): Int = foodList.size

    fun updateList(newList: List<Food>) {
        foodList = newList
        notifyDataSetChanged()
    }
}
