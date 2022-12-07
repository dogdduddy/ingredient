package com.example.ingredient.src.expirationDate.add_ingredient

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.databinding.ItemAddIngredientBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddIngredientViewHolder(val binding:ItemAddIngredientBinding)
    :RecyclerView.ViewHolder(binding.root) {

    fun bindWithView(ingredient:Ingredient) {
        Glide.with(itemView)
            .load(ingredient.ingredienticon)
            .into(binding.icIngredientIcon)
        binding.tvIngredientName.text = ingredient.ingredientname
    }
}