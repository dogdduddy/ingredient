package com.example.data.repository

import com.example.data.mapper.RecipeMapper
import com.example.data.model.RecipeResponse
import com.example.data.remote.RecipeDataSource
import com.example.domain.repository.RecipeRepository
import javax.inject.Inject


class RecipeRepositoryImpl @Inject constructor(
	private val recipeDataSource: RecipeDataSource
): RecipeRepository {
	override suspend fun getRecipe(): com.example.domain.model.RecipeResponse {
		return RecipeMapper.mapperRecipe(recipeDataSource.getRecipe())
	}

	override suspend fun getRecipes(): List<com.example.domain.model.RecipeResponse> {
		TODO("Not yet implemented")
	}

}