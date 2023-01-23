package com.example.presentation.feature.expirationDate.add_ingredient.models

import java.util.*

data class ExpiryDateIngredient(
    var ingredient:Ingredient? = null,
    var expirydate:Int = 0,
    var ingredientstatus:Int = 0,
    var storagestatus:Int = 0,
    var selected:Boolean = false,
    var addedDate: Date? = null,
    var discard: Date? = null
)
