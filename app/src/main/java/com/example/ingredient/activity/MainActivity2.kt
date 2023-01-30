package com.example.ingredient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import com.example.ingredient.R
import com.example.ingredient.src.foodbook.models.Recipe
import com.google.firebase.firestore.FirebaseFirestore
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


		/*
		// Event 콜렉션에 데이터 넣기
//		title
//		effectcolor
//		effectrange
//		effectstyle
//		eventidx
//		recipelist

		var insertBtn = findViewById<Button>(R.id.insertBtn)
		var transBtn = findViewById<Button>(R.id.transBtn)
		transBtn.setOnClickListener {
			CoroutineScope(Dispatchers.IO).launch {
				var seachRecipe = listOf<String>("두부전골", "호떡", "칼국수")
				var recipeList = FirebaseFirestore.getInstance().collection("Recipes")
					.whereIn("name", seachRecipe)
					.get().await().toMutableList()
				var temp = recipeList[0]
			}
		}
		insertBtn.setOnClickListener {
			CoroutineScope(Dispatchers.IO).launch {
				var seachRecipe = listOf<String>("두부전골", "호떡", "칼국수")
				var recipeList = FirebaseFirestore.getInstance().collection("Recipes")
					.whereIn("name", seachRecipe)
					.get().await().toMutableList().map { it.data }
				var mapof = mapOf(
					"title" to "추운 겨울을 따뜻하게 보낼 레시피",
					"effectcolor" to "#3366FF",
					"effectstyle" to "bold",
					"effectrange" to "0,5",
					"eventidx" to "2",
					"recipelist" to recipeList
				)
				FirebaseFirestore.getInstance().collection("Event").document()
					.set(mapof)
			}
		}
		 */
	}
}