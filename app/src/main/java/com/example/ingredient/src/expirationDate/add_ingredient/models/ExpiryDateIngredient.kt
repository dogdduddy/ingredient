package com.example.ingredient.src.expirationDate.add_ingredient.models

data class ExpiryDateIngredient(
    var ingredient:Ingredient,
    var expirydate:Int,
    var storagestatus:Int,
    var ingredientstatus:Int
)
