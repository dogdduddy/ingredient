package com.example.data.mapper

import com.example.data.model.RecipeResponse

object RecipeMapper {
	fun mapperRecipe(response: RecipeResponse): com.example.domain.model.RecipeResponse {
		return com.example.domain.model.RecipeResponse(
			description = response.description,
			icon = response.icon,
			ingredient = response.ingredient,
			level = response.level,
			like = response.like,
			link = response.link,
			name = response.name,
			subscribe = response.subscribe,
			time = response.time,
			title = response.title
		)
	}
}