package com.example.ingredient.src.foodbook.models

data class EventTab(
	var title:String = "",
	var effectcolor:String = "#000000",
	var effectrange:String = "",
	var effectsrtyle :String = "",
	var eventidx :String = "",
	var recipelist: MutableList<Recipe> = mutableListOf()
)
