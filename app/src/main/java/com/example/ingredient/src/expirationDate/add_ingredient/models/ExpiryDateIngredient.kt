package com.example.ingredient.src.expirationDate.add_ingredient.models

data class ExpiryDateIngredient(
    var ingredient:Ingredient,
    var expirydate:Int,
    var ingredientstatus:Int,
    var storagestatus:Int,
    var selected:Boolean
)
