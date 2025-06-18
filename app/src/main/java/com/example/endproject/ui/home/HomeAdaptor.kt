package com.example.endproject.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.endproject.R
import com.example.endproject.data.food.Food
import com.example.endproject.databinding.HomeRecycleBinding

class HomeAdaptor(private var foodList: List<Food>) :
    RecyclerView.Adapter<HomeAdaptor.HomeViewHolder>() {

    class HomeViewHolder(val binding: HomeRecycleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeRecycleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
//        val white =  ContextCompat.getColor(holder.itemView.context,R.color.white);
//        holder.itemView.setBackgroundColor(white); //배경 하얀색으로 다 바꾸기

        val food = foodList[position]
        holder.binding.foodNameText.text = food.name
        holder.binding.foodKcalText.text = String.format("%.1f kcal", food.calories)
        holder.binding.foodHomeProtein.text = String.format("%.1f g", food.protein)
        holder.binding.foodHomeCarb.text = String.format("%.1f g", food.carb)
        holder.binding.foodHomeFat.text = String.format("%.1f g", food.fat)
        holder.binding.foodHomeGram.text = "${food.grams}g"

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