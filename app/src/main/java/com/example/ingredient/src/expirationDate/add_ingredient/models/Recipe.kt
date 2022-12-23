package com.example.ingredient.src.expirationDate.add_ingredient.models


data class RecipeTemp(
	var description: String = "",
	var icon: String = "",
	var fulltext: List<String>? = null,
	var ingredient: List<String>? = null,
	var level: String = "",
	var like: String = "",
	var link: String = "",
	var name: String = "",
	var subscribe: String = "",
	var time: String = "",
	var title: String = ""
)
