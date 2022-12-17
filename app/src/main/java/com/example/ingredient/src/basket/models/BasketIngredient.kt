package com.example.ingredient.src.basket.models


// 재료 (아이콘, 재료id, 재료명, 카테고리, 그룹명, 수량)

data class BasketIngredient(
    val ingredientIcon: String,
    val ingredientIdx: Int,
    val ingredientName: String,
    val categoryName: String,
    val groupName: String,
    val quantity: Int
)
