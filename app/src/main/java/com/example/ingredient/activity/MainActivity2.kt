package com.example.ingredient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class CategoryIngrediets2(
	var categoryid:Int= 0,
	var categoryname: String ="",
	var ingredientlist: List<Ingredient2>? = null
)
data class Ingredient2(
	val ingredienticon: String? = null,
	val ingredientidx: Int? = null,
	val ingredientname: String? = null,
	val ingredientcategory: String?= null
)
class MainActivity2 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main2)

		/*
		val db = FirebaseFirestore.getInstance()
		CoroutineScope(Dispatchers.IO).launch {
		var temp = db.collection("Category")
			.whereEqualTo("categoryname", "기타")
			.get()
			.await()
			.toList()

		temp.forEachIndexed { index, queryDocumentSnapshot ->
			Log.d("testT", queryDocumentSnapshot.get("categoryname").toString())
			var listRef = queryDocumentSnapshot.data?.get("ingredientlist") as List<DocumentReference>
			var ingnredientList = mutableListOf<Ingredient>()
			Log.d("testT", ingnredientList.toString())
				listRef.forEach { documentReference ->
					var ingredient = documentReference.get().await().toObject<Ingredient>()
					ingnredientList.add(ingredient!!)
				}
				queryDocumentSnapshot.reference.update(mapOf("ingredientlist" to ingnredientList))
			}
		}

		 */

		val db = FirebaseFirestore.getInstance()
		db.collection("Category")
			.get()
			.addOnSuccessListener { documents ->
				documents.documents.forEach { doc ->
					var temp = doc.toObject<CategoryIngrediets>()
					Log.d("test", temp.toString())
				}
			}
	}
}