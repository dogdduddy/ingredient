package com.dogdduddy.ingredient.src.basket
import com.dogdduddy.ingredient.src.basket.models.BasketIngredient

interface BasketView {
	fun onGetBasketSuccess(response: ArrayList<BasketIngredient>)
	fun onGetBasketFailure(message: String)

	fun itemDeleteListener()

	fun onGetBasketGroupSuccess(response: ArrayList<String>)
	fun onGetBasketGroupFailure(message: String)
	fun onPostBasketGroupSuccess()
	fun onPostBasketGroupFailure(message: String)

	fun onDeleteBasketGroupListSuccess(response: String)
	fun onDeleteBasketGroupIngredientSuccess(response: String, position: Int)
	fun onDeleteBasketGroupListFailure(message: String)
	fun onDeleteBasketGroupIngredientFailure(message: String)
}