package com.example.ingredient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.add_ingredient.models.RecipeTemp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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


		// FoodCategory 레시피 데이터 Preference to Recipe::Class.java
		val db = FirebaseFirestore.getInstance()
		CoroutineScope(Dispatchers.IO).launch {
			var temp = db.collection("FoodCategory")
				.get()
				.await()
				.toList()

			temp.forEachIndexed { index, queryDocumentSnapshot ->
				var listRef = queryDocumentSnapshot.data?.get("recipes") as List<DocumentReference>
				var recipeList = mutableListOf<RecipeTemp>()

				listRef.forEach { documentReference ->
					var recipe = documentReference.get().await().toObject(RecipeTemp::class.java)
					recipeList.add(recipe!!)
				}
				queryDocumentSnapshot.reference.update(mapOf("recipes" to recipeList))
			}
		}
		 */
	}

}