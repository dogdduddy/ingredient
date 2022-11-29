package com.example.ingredient.src.basket

import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BasketService(val view:BasketView) {
	private val database = FirebaseFirestore.getInstance()
	private val userid = FirebaseAuth.getInstance().currentUser?.uid
	fun getBasket() {
		var basketData = ArrayList<BasketIngredient>()
		database.collection("ListData")
			.document(userid!!)
			.collection("Basket")
			.get()
			.addOnSuccessListener { documents ->
				for(document in documents) {
					basketData.add(
						BasketIngredient(
						document.get("ingredienticon").toString(),
						document.get("ingredientidx").toString().toInt(),
						document.get("ingredientname").toString(),
						document.get("ingredientcategory").toString(),
						document.get("groupName").toString(),
						document.get("ingredientquantity").toString().toInt()
						)
					)
				}
				view.onGetFridgeSuccess(basketData)
			}
			.addOnFailureListener {
				view.onGetFridgeFailure(it.message ?: "통신 오류")
			}
	}
}