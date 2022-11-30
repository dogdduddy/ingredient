package com.example.ingredient.src.basket.group.addGroup.addIngredient

import android.util.Log
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class IngredientService(val view: AddIngredientView) {
	private val database = FirebaseFirestore.getInstance()
	fun GetCategoryIngrediets() {
		// 검색 통해 나온 레시피명을 담는 리스트
		database.collection("Category").get().addOnSuccessListener { documents ->
			Log.d("testT", "inGetCategoryIngrediets")
			var response = ArrayList<CategoryIngrediets>()
			var sortedDocs = documents.sortedBy { it.get("categoryid").toString().toInt() }

			var ingredientListRef = ArrayList<List<DocumentReference>>()
			for (document in sortedDocs) {
				var ingredientlist = arrayListOf<Ingredient>()
				document.data.apply {
					ingredientListRef.add(get("ingredientlist") as List<DocumentReference>)
					var category = CategoryIngrediets(
						get("categoryid").toString().toInt(),
						get("categoryname").toString(),
						ingredientlist
					)
					response.add(category)
				}
			}
			view.onGetCategoryIngredietSuccess(response)
			CoroutineScope(Dispatchers.Main).launch {
				launch {
					ingredientListRef.forEachIndexed { index, documentReferences ->
						var ingredientlist = arrayListOf<Ingredient>()
						documentReferences.forEach { documentReference ->
							Log.d("testT", "Next - in forEach")
							documentReference.get().addOnSuccessListener { documentSnapshot ->
								ingredientlist.add(
									Ingredient(
										documentSnapshot.get("ingredienticon").toString(),
										documentSnapshot.get("ingredientidx").toString().toInt(),
										documentSnapshot.get("ingredientname").toString(),
										documentSnapshot.get("ingredientcategory").toString()
									)
								)
							}.await()
							Log.d("testT", "Next - in forEach Data ${ingredientlist}")
						}
						response[index].ingredientList = ingredientlist
					}
				}.join()

				Log.d("testT", "testResponse: $response")
				view.test(response)
			}
		}
	}
	/*
	fun GetCategoryIngrediets() {
		// 검색 통해 나온 레시피명을 담는 리스트
		database.collection("Category").get().addOnSuccessListener { documents ->
			Log.d("testT", "inGetCategoryIngrediets")
				var response = ArrayList<CategoryIngrediets>()
				var sortedDocs = documents.sortedBy { it.get("categoryid").toString().toInt() }
				for (document in sortedDocs) {
					Log.d("testT", "insortedDocs")
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
				Log.d("testT", "Service : $response")
				view.onGetCategoryIngredietSuccess(response)
			}
	}

	 */
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