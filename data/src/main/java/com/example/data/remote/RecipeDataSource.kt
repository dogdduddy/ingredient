package com.example.data.remote

import com.example.data.model.RecipeResponse

interface RecipeDataSource {
	suspend fun getRecipe(): RecipeResponse
	suspend fun getRecipes(): List<RecipeResponse>
}