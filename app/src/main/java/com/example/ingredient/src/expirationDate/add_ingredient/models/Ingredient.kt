package com.example.ingredient.src.expirationDate.add_ingredient.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

/*
// Parceler를 이용한 방법 연구 중
// 참고 : https://developer.android.com/kotlin/parcelize?hl=ko
@Parcelize
data class Ingredient(val ingredientIcon: String, val ingredientIdx: Int, val ingredientName: String): Parcelable {
    companion object :Parceler<Ingredient>{
        override fun create(parcel: Parcel):Ingredient {return Ingredient(parcel.readString()!!, parcel.readInt(), parcel.readString()!!)}
        override fun newArray(size: Int): Array<Ingredient> {
            return super.newArray(size)
        }
        override fun Ingredient.write(parcel: Parcel, flags: Int) {
            parcel.writeString(ingredientIcon)
            parcel.writeInt(ingredientIdx)
            parcel.writeString(ingredientName)
        }
    }
    override fun describeContents(): Int {
        return hashCode()
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(ingredientIcon)
        dest!!.writeInt(ingredientIdx)
        dest!!.writeString(ingredientName)
    }
}
 */

// 참고 https://www.appsloveworld.com/kotlin/100/56/android-os-badparcelableexception-parcelable-protocol-requires-a-parcelable-crea

@Parcelize
data class Ingredient(val ingredientIcon: String, val ingredientIdx: Int, val ingredientName: String): Parcelable {
    constructor(src:Parcel): this(src.readString()!!, src.readInt(), src.readString()!!)
    companion object CREATOR : Parcelable.Creator<Ingredient>{
        override fun createFromParcel(source: Parcel?): Ingredient {
            return Ingredient(source!!)
        }
        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }
    }
    override fun describeContents(): Int {
        return hashCode()
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(ingredientIcon)
        dest!!.writeInt(ingredientIdx)
        dest!!.writeString(ingredientName)
    }
}


