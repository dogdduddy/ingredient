package com.example.ingredient.src.expirationDate.add_ingredient.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CategoryIngrediets(
    var ingredientCategoryIdx:Int = 0,
    var ingredientCategoryName: String = "",
    var ingredientList: List<Ingredient> = listOf()
)

/*
package com.example.ingredient.src.expirationDate.add_ingredient.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryIngrediets(
    @SerializedName("ingredientCategoryIdx")
    @get: PropertyName("categoryid")
    val ingredientCategoryIdx:Int,
    @SerializedName("ingredientCayegoryName")
    @get: PropertyName("categoryname")
    val ingredientCategoryName: String,
    @SerializedName("ingredientList")
    @get: PropertyName("ingredientlist")
    var ingredientList: List<Ingredient>?
):Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }
}

 */