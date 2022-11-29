package com.example.ingredient.src.basket
import com.example.ingredient.src.basket.models.BasketIngredient

interface BasketView {
	fun onGetFridgeSuccess(response: ArrayList<BasketIngredient>)
	fun onGetFridgeFailure(message: String)
}