package com.example.ingredient.feature.expirationDate.add_ingredient.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient(
    @SerializedName("ingredienticon")
    val ingredienticon: String = "",
    @SerializedName("ingredientidx")
    val ingredientidx: Int = 0,
    @SerializedName("ingredientname")
    val ingredientname: String = "",
    @SerializedName("ingredientcategory")
    val ingredientcategory: String = ""
): Parcelable



