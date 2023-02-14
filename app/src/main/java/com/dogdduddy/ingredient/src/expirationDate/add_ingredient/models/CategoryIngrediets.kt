package com.dogdduddy.ingredient.src.expirationDate.add_ingredient.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryIngrediets(
    @SerializedName("categoryid")
    var categoryid:Int = 0,
    @SerializedName("categoryname")
    var categoryname: String = "",
    @SerializedName("ingredientlist")
    var ingredientlist: List<Ingredient>? = null
): Parcelable

