package com.example.ingredient.src.basket.group.addGroup.addIngredient

import android.util.Log
import com.example.ingredient.src.expirationDate.add_ingredient.AddIngredientViewPagerAdapter
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class IngredientService(val view: AddIngredientView) {
	private val database = FirebaseFirestore.getInstance()
	private val uid = FirebaseAuth.getInstance().uid!!
	// Category Init
	fun GetCategoryIngrediets() {
		database.collection("Category").get().addOnSuccessListener { documents ->
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
			var ingredientListDocID = ArrayList<List<String>>()
			ingredientListRef.forEachIndexed { index, v ->
				ingredientListDocID.add(v.map { it.id })
			}
			view.onGetCategoryIngredietSuccess(response)
			CoroutineScope(Dispatchers.Main).launch {
				launch {
					ingredientListDocID.forEachIndexed { index, documents ->
						var responseList = mutableListOf<QueryDocumentSnapshot>()
						for (i in 0 until documents.size step 10) {
							var temp = listOf<String>()
							if (i / 10 == documents.size / 10) {
								temp = documents.subList(i, documents.size)
							} else {
								temp = documents.subList(i, i + 10)
							}
							responseList.addAll(
								database.collection("ingredients")
									.whereIn(FieldPath.documentId(), temp)
									.get().await().toMutableList()
							)
						}
						var ingredientlist = arrayListOf<Ingredient>()
						responseList.forEach { doc ->
							ingredientlist.add(
								Ingredient(
									doc.get("ingredienticon").toString(),
									doc.get("ingredientidx").toString().toInt(),
									doc.get("ingredientname").toString(),
									doc.get("ingredientcategory").toString()
								)
							)
						}
						response[index].ingredientlist = ingredientlist
					}
				}.join()
				view.onGetCategoryIngredietListSuccess(response)
			}
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
	fun PostIngredients(group:String, pickingredients: MutableList<Ingredient>) {
		database.collection("ListData")
			.document(uid)
			.collection("Basket")
			.whereEqualTo("groupName", group)
			.get()
			.addOnSuccessListener { documents ->
				// 해당 그룹에 재료가 없을 때
				if(documents.isEmpty()) {
					pickingredients.forEach {
						var hash = hashMapOf(
							"ingredienticon" to it.ingredienticon,
							"ingredientidx" to it.ingredientidx,
							"ingredientname" to it.ingredientname,
							"ingredientcategory" to it.ingredientcategory,
							"groupName" to group,
							"ingredientquantity" to 1
						)
						database.collection("ListData")
							.document(FirebaseAuth.getInstance().uid!!)
							.collection("Basket")
							.document()
							.set(hash)
					}
				}
				// 해당 그룹에 재료가 있을 때
				else {
					var values = arrayListOf<Ingredient>()
					for (document in documents) {
						document.data.apply {
							values.add(
								Ingredient(
									get("ingredienticon").toString(),
									get("ingredientidx").toString().toInt(),
									get("ingredientname").toString(),
									get("ingredientcategory").toString()
								)
							)
						}
					}
					pickingredients.forEachIndexed { index, ingredient ->
						var number = values.indexOf(ingredient)
						// 중복 재료가 있을 때
						if(number != -1) {
							Log.d("AddTest", "Match Data")
							database.collection("ListData")
								.document(uid)
								.collection("Basket")
								.document(documents.documents[number].reference.id)
								.update(
									mapOf(
										"ingredientquantity" to documents.documents[number].data!!.get(
											"ingredientquantity"
										).toString().toInt() + 1
									)
								)
						}
						// 중복 재료가 없을 때
						else {
							var ingredient = pickingredients[index]
							var hash = hashMapOf(
								"ingredienticon" to ingredient.ingredienticon,
								"ingredientidx" to ingredient.ingredientidx,
								"ingredientname" to ingredient.ingredientname,
								"ingredientcategory" to ingredient.ingredientcategory,
								"groupName" to group,
								"ingredientquantity" to 1
							)
							database.collection("ListData")
								.document(uid)
								.collection("Basket")
								.document()
								.set(hash)
						}
					}
				}
				view.onPostGroupIngredientSuccess()
			}
	}
}