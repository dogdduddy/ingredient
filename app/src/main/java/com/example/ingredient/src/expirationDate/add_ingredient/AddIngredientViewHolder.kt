package com.example.ingredient.src.expirationDate.add_ingredient

import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.databinding.ItemIngredientBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient

class AddIngredientViewHolder(val binding:ItemIngredientBinding)
    :RecyclerView.ViewHolder(binding.root) {
    val clickItemPosition = 0

    fun bindWithView(ingredient:Ingredient) {
        when (ingredient.ingredientIcon) {
            "carrot" -> binding.icIngredientIcon.setImageResource(R.drawable.buckwheat)
            "potato" -> binding.icIngredientIcon.setImageResource(R.drawable.bibim)
            "chicken" -> binding.icIngredientIcon.setImageResource(R.drawable.ham)
            "fish" -> binding.icIngredientIcon.setImageResource(R.drawable.salad)
        }
        binding.tvIngredientName.text = ingredient.ingredientName
    }
}