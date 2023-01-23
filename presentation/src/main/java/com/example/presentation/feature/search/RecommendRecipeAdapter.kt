package com.example.presentation.feature.search

import android.content.Context
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.feature.foodbook.models.Recipe

class RecommendRecipeAdapter : RecyclerView.Adapter<RecommendRecipeAdapter.ViewHolder>()
{
	private var recommendList: MutableList<Recipe>  = mutableListOf()
	private var context: Context? = null
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view =
			LayoutInflater.from(parent!!.context).inflate(R.layout.item_main_recommend, parent, false)
		context = view.context
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.itemView.setOnClickListener { itemCLickListener.onClick(it, position) }
		holder.image.clipToOutline = true
		Glide.with(holder.itemView)
			.load(recommendList[position].icon)
			.into(holder.image)

		holder.title.text = recommendList[position].title
		var span: Spannable = holder.title.text as Spannable
		span.setSpan(ForegroundColorSpan(context!!.getColor(R.color.orange_300)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
		holder.subtitle.text = recommendList[position].description
	}

	override fun getItemCount(): Int {
		return recommendList.size
	}

	fun submitList(recommendList: MutableList<Recipe>) {
		this.recommendList = recommendList
		notifyDataSetChanged()
	}

	inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
		internal var image: ImageView
		internal var title: TextView
		internal var subtitle: TextView
		init {
			var context = context
		}
		init {
			image = view.findViewById(R.id.main_recommend_image)
			title = view.findViewById(R.id.main_recommend_title)
			subtitle = view.findViewById(R.id.main_recommend_subtitle)
		}
	}
	// Adapter
	interface OnItemClickListener {
		fun onClick(view: View, position: Int)
	}

	fun setItemClickListener(onItemClickListener: OnItemClickListener) {
		this.itemCLickListener = onItemClickListener
	}
	private lateinit var itemCLickListener: OnItemClickListener
}