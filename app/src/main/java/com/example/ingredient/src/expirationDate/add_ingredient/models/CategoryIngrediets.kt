package com.example.ingredient.src.expirationDate.add_ingredient.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryIngrediets(
    @SerializedName("ingredientCategoryIdx")
    var ingredientCategoryIdx:Int,
    @SerializedName("ingredientCategoryName")
    var ingredientCategoryName: String,
    @SerializedName("ingredientList")
    var ingredientList: List<Ingredient>?
): Parcelable

