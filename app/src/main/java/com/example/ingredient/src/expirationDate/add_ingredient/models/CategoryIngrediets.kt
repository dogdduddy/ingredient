package com.example.ingredient.src.expirationDate.add_ingredient.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryIngrediets(
    @SerializedName("ingredientCategoryIdx")
    val ingredientCategoryIdx:Int,
    @SerializedName("ingredientCayegoryName")
    val ingredientCategoryName: String,
    @SerializedName("ingredientList")
    val ingredientList: List<Ingredient>?
):Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }
}
