package com.example.ingredient.src.expirationDate.add_ingredient.models

import java.time.LocalDate
import java.util.*

data class ExpiryDateIngredient(
    var ingredient:Ingredient,
    var expirydate:Int,
    var ingredientstatus:Int,
    var storagestatus:Int,
    var selected:Boolean,
    var addedDate: Date,
    var discard: Date
)
