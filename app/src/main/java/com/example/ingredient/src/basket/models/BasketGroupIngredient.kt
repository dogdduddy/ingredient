package com.example.ingredient.src.basket.models

//그룹 속 재료 (아이콘, 카테고리, 재료명, 수량)

data class BasketGroupIngredient(
    val ingredientIcon: String,
    val ingredientIdx: Int,
    val ingredientName: String,
    val categoryName: String,
    val quantity:Int
)
