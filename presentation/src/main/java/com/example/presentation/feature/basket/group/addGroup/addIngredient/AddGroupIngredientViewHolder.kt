package com.example.presentation.feature.basket.group.addGroup.addIngredient

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.databinding.ItemAddGroupingredientBinding
import com.example.ingredient.feature.expirationDate.add_ingredient.models.Ingredient

class AddGroupIngredientViewHolder(val binding: ItemAddGroupingredientBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindWithView(ingredient: Ingredient) {
        Glide.with(itemView)
            .load(ingredient.ingredienticon)
            .into(binding.groupAddIngredientIcon)
        binding.groupAddIngredientName.text = ingredient.ingredientname
    }
}