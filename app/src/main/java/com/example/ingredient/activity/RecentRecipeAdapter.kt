package com.example.ingredient.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.search.SearchAdapter

class RecentRecipeAdapter : RecyclerView.Adapter<RecentRecipeAdapter.ViewHolder>() {
	private var context: Context? = null
	private var recipeList = ArrayList<MutableMap<String, String>>()


	inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
		internal var title: TextView
		internal var food: ImageView
		init {
			var context = context
		}
		init {
			title = view.findViewById(R.id.nav_recent_name)
			food = view.findViewById(R.id.nav_recent_image)
		}
	}
	fun submitList(recipeList: ArrayList<MutableMap<String, String>>) {
		this.recipeList.clear()
		this.recipeList.addAll(recipeList)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view =
			LayoutInflater.from(parent!!.context).inflate(R.layout.item_nav_recent, parent, false)
		context = view.context
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.title.text = recipeList[position]["name"]
		Glide.with(holder.itemView)
			.load(recipeList[position]["image"])
			.into(holder.food)

	}

	override fun getItemCount(): Int = recipeList.size
}