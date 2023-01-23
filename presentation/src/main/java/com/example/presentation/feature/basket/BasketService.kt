package com.example.presentation.feature.basket

import android.util.Log
import com.example.ingredient.feature.basket.models.BasketIngredient
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
					Log.d("basketTest", "Basket : ${document.get("ingredientidx")}")
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
		database.collection("ListData")
			.document(userid!!)
			.collection("BasketList")
			.get()
			.addOnSuccessListener { documents ->
				var basketGroup = ArrayList<String>()
				for(document in documents) {
					basketGroup.add(document.get("groupName").toString())
				}
				Log.d("basketTest", "basketGroup : $basketGroup")
				view.onGetBasketGroupSuccess(basketGroup)
			}
			.addOnFailureListener {
				view.onGetBasketGroupFailure(it.message ?: "통신 오류")
			}
	}

	fun deleteBasketGroup(groupName: String, position: Int) {
		database.collection("ListData")
			.document(userid!!)
			.collection("Basket")
			.whereEqualTo("groupName", groupName)
			.get()
			.addOnSuccessListener {
				for(document in it) {
					document.reference.delete()
				}
				view.onDeleteBasketGroupListSuccess(groupName)
			}
			.addOnFailureListener { view.onDeleteBasketGroupListFailure(it.message ?: "통신오류") }
		database.collection("ListData")
			.document(userid!!)
			.collection("BasketList")
			.whereEqualTo("groupName", groupName)
			.get()
			.addOnSuccessListener {
				for(document in it) {
					document.reference.delete()
				}
				view.onDeleteBasketGroupIngredientSuccess(groupName, position)
			}
			.addOnFailureListener { view.onDeleteBasketGroupIngredientFailure(it.message ?: "통신오류") }
	}

	fun postBasketGroup(groupName: String) {
		database.collection("ListData")
			.document(FirebaseAuth.getInstance().uid.toString())
			.collection("BasketList")
			.document()
			.set(hashMapOf("groupName" to groupName))
			.addOnSuccessListener {
				view.onPostBasketGroupSuccess()
			}
	}
}