package com.example.ingredient.feature.foodbook.models

data class EventTab(
	var title:String = "",
	var effectcolor:String = "#000000",
	var effectrange:String = "",
	var effectstyle :String = "",
	var eventidx :String = "",
	var recipelist: MutableList<Recipe> = mutableListOf()
)
