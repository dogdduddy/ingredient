package com.example.ingredient.src.foodbook.models

data class FoodCategory(
	var categoryid: Int = 0,
	var categoryname: String = "",
	var recipes: List<Recipe>? = null
)
