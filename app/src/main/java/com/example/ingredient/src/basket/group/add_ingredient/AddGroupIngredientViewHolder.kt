package com.example.ingredient.src.basket.group.add_ingredient

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.databinding.ItemAddGroupingredientBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient

class AddGroupIngredientViewHolder(val binding: ItemAddGroupingredientBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindWithView(ingredient: Ingredient) {
        Glide.with(itemView)
            .load(ingredient.ingredientIcon)
            .into(binding.groupAddIngredientIcon)
        binding.groupAddIngredientName.text = ingredient.ingredientName
    }
}