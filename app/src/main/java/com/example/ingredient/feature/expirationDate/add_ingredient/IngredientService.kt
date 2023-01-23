package com.example.ingredient.feature.expirationDate.add_ingredient

import com.example.ingredient.feature.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.feature.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class IngredientService(val view: AddIngredientView) {
	private val database = FirebaseFirestore.getInstance()
	private val uid = FirebaseAuth.getInstance().uid!!
	// Category Init
	fun GetCategoryIngrediets() {
		CoroutineScope(Dispatchers.Main ).launch {
			var result = database.collection("Category")
				.get()
				.await()
				.toObjects(CategoryIngrediets::class.java)
			var response = arrayListOf<CategoryIngrediets>()
			response.addAll(result.sortedBy { it.categoryid })
			view.onGetCategoryIngredietSuccess(response)
		}
	}
	// 재료 검색
	fun GetSearchCategoryIngrediets(keyword: String, ingredients: ArrayList<CategoryIngrediets>) {
		//database.collection("ingredients").orderBy("ingredientname").startAt(keyword).endAt(keyword+ "\uf8ff")
		database.collection("ingredients").whereEqualTo("ingredientname", keyword)
			.get()
			.addOnSuccessListener { doc ->
				var response = ArrayList<CategoryIngrediets>()
				var sortedDocs = doc.sortedBy { it.get("categoryid").toString().toInt() }
				var ingredientList = mutableListOf<Ingredient>()
				sortedDocs.forEach {
					ingredientList.add(
						Ingredient(
							it.get("ingredienticon").toString(),
							it.get("ingredientidx").toString().toInt(),
							it.get("ingredientname").toString(),
							it.get("ingredientcategory").toString()
						)
					)
				}
				ingredients.forEachIndexed { i, v ->
					response.add(
						CategoryIngrediets(
							v.categoryid,
							v.categoryname,
							if(i==0) ingredientList else v.ingredientlist
						)
					)
				}
				view.onGetSearchCategoryIngredietSuccess(response)
			}
	}
	fun PostIngredients(pickingredients: MutableList<Ingredient>) {
		TODO("Not Yet")
	}
}