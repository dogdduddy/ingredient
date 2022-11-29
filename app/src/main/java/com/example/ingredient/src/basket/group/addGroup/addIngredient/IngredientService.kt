package com.example.ingredient.src.basket.group.addGroup.addIngredient

import android.util.Log
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class IngredientService(val view: AddIngredientView) {
	private val database = FirebaseFirestore.getInstance()
	fun GetCategoryIngrediets() {
		// 검색 통해 나온 레시피명을 담는 리스트
		database.collection("Category").get().addOnSuccessListener { documents ->
				var response = ArrayList<CategoryIngrediets>()
				var sortedDocs = documents.sortedBy { it.get("categoryid").toString().toInt() }
				for (document in sortedDocs) {
					var ingredientlist = arrayListOf<Ingredient>()
					document.data.apply {
						(get("ingredientlist") as List<DocumentReference>).forEach {
							it.get().addOnSuccessListener { document ->
								ingredientlist.add(
									Ingredient(
										document.get("ingredienticon").toString(),
										document.get("ingredientidx").toString().toInt(),
										document.get("ingredientname").toString(),
										document.get("ingredientcategory").toString()
									)
								)
							}
						}
						var category = CategoryIngrediets(
							get("categoryid").toString().toInt(),
							get("categoryname").toString(),
							ingredientlist
						)
						response.add(category)
					}
				}
				view.onGetCategoryIngredietSuccess(response)
			}
	}
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
							v.ingredientCategoryIdx,
							v.ingredientCategoryName,
							if(i==0) ingredientList else v.ingredientList
						)
					)
				}
				view.onGetSearchCategoryIngredietSuccess(response)
			}
	}
}