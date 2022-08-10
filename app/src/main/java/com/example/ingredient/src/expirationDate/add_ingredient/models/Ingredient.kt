package com.example.ingredient.src.expirationDate.add_ingredient.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient(
    @SerializedName("ingredientIcon")
    val ingredientIcon: String,
    @SerializedName("ingredientIdx")
    val ingredientIdx: Int,
    @SerializedName("ingredientName")
    val ingredientName: String
): Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }
}
