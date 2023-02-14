package com.dogdduddy.ingredient.src.expirationDate.add_ingredient

import com.dogdduddy.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets

interface AddIngredientView {
	fun onGetCategoryIngredietSuccess(response: ArrayList<CategoryIngrediets>)
	fun onGetCategoryIngredietFailure(message: String)

	fun onGetSearchCategoryIngredietSuccess(response: ArrayList<CategoryIngrediets>)
	fun onGetSearchCategoryIngredietFailure(message: String)

	fun onGetCategoryIngredietListSuccess(response: ArrayList<CategoryIngrediets>)
	fun onGetCategoryIngredietListFailure(message: String)

	fun onPostGroupIngredientSuccess()
	fun onPostGroupIngredientFailure(message: String)
}