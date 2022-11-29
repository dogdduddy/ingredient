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
				view.onGetBasketSuccess(basketData)
			}
			.addOnFailureListener {
				view.onGetBasketFailure(it.message ?: "통신 오류")
			}
	}

	fun getBasketGroup() {
		var basketGroup = ArrayList<String>()
		database.collection("ListData")
			.document(userid!!)
			.collection("BasketList")
			.get()
			.addOnSuccessListener { documents ->
				for(document in documents) {
					basketGroup.add(document.get("groupName").toString())
				}
				view.onGetBasketGroupSuccess(basketGroup)
			}
			.addOnFailureListener {
				view.onGetBasketGroupFailure(it.message ?: "통신 오류")
			}
	}

	fun deleteBasketGroup(groupname: String) {
		database.collection("ListData")
			.document(userid!!)
			.collection("Basket")
			.whereEqualTo("groupName", groupname)
			.get()
			.addOnSuccessListener {
				for(document in it) {
					document.reference.delete()
				}
				view.onDeleteBasketGroupListSuccess(groupname)
			}
			.addOnFailureListener { view.onDeleteBasketGroupListFailure(it.message ?: "통신오류") }
		database.collection("ListData")
			.document(userid!!)
			.collection("BasketList")
			.whereEqualTo("groupName", groupname)
			.get()
			.addOnSuccessListener {
				for(document in it) {
					document.reference.delete()
				}
				view.onDeleteBasketGroupIngredientSuccess(groupname)
			}
			.addOnFailureListener { view.onDeleteBasketGroupIngredientFailure(it.message ?: "통신오류") }
	}
}