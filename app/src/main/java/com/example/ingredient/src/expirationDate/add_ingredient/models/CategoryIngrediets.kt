package com.example.ingredient.src.expirationDate.add_ingredient.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryIngrediets(
    @SerializedName("ingredientCategoryIdx")
    @get:PropertyName("categoryid")
    var ingredientCategoryIdx:Int = 0,
    @SerializedName("ingredientCayegoryName")
    @get:PropertyName("categoryname")
    var ingredientCategoryName: String = "",
    @SerializedName("ingredientList")
    @get:PropertyName("ingredientlist")
    var ingredientList: List<Ingredient> = listOf()
):Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }
}
