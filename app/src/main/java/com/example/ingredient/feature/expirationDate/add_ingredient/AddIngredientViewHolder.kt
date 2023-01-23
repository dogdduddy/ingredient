package com.example.ingredient.feature.expirationDate.add_ingredient

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.databinding.ItemAddIngredientBinding
import com.example.ingredient.feature.expirationDate.add_ingredient.models.Ingredient

class AddIngredientViewHolder(val binding:ItemAddIngredientBinding)
    :RecyclerView.ViewHolder(binding.root) {

    fun bindWithView(ingredient:Ingredient) {
        Glide.with(itemView)
            .load(ingredient.ingredienticon)
            .into(binding.icIngredientIcon)
        binding.tvIngredientName.text = ingredient.ingredientname
    }
}