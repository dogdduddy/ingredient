package com.example.ingredient.src.basket.models

//그룹 (그룹명, 재료 리스트)

data class BasketGroup(
    val categoryName: String,
    var ingredientList: ArrayList<BasketIngredient>
)
