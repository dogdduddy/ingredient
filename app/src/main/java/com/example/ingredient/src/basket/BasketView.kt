package com.example.ingredient.src.basket
import com.example.ingredient.src.basket.models.BasketIngredient

interface BasketView {
	fun onGetBasketSuccess(response: ArrayList<BasketIngredient>)
	fun onGetBasketFailure(message: String)

	fun onGetBasketGroupSuccess(response: ArrayList<String>)
	fun onGetBasketGroupFailure(message: String)

	fun onDeleteBasketGroupListSuccess(response: String)
	fun onDeleteBasketGroupIngredientSuccess(response: String)
	fun onDeleteBasketGroupListFailure(message: String)
	fun onDeleteBasketGroupIngredientFailure(message: String)
}