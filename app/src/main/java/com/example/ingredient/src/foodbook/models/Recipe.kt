package com.example.ingredient.src.foodbook.models

data class Recipe(
	var description: String = "",
	var icon: String = "",
	var ingredient: List<String>? = null,
	var level: String = "",
	var like: String = "",
	var link: String = "",
	var name: String = "",
	var subscribe: String = "",
	var time: String = "",
	var title: String = ""
)

