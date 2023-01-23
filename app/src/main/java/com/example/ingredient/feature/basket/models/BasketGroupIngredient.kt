package com.example.ingredient.feature.basket.models

//그룹 속 재료 (아이콘, 카테고리, 재료명, 수량)

data class BasketGroupIngredient(
    val ingredienticon: String,
    val ingredientidx: Int,
    val ingredientname: String,
    val ingredientcategory: String,
    var ingredientquantity: Int
)
