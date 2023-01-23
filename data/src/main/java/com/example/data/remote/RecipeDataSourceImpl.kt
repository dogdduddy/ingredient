package com.example.data.remote

import com.example.data.model.RecipeResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecipeDataSourceImpl(
	private val db: FirebaseFirestore
):RecipeDataSource {
	override suspend fun getRecipe(): RecipeResponse {
		return db.collection("recipes")
			.document("recipe")
			.get()
			.await()
			.toObject(RecipeResponse::class.java)!!
	}

	override suspend fun getRecipes(): List<RecipeResponse> {
		TODO("Not yet implemented")
	}
}