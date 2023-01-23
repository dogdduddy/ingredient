package com.example.presentation.feature.search

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.databinding.ItemRecommendTabBinding
import com.example.ingredient.feature.foodbook.models.EventTab
import com.example.ingredient.feature.foodbook.models.Recipe

class SearchMainAdapter ()
    : RecyclerView.Adapter<SearchMainAdapter.ViewHolder>()
{
    private var eventTabList: MutableList<EventTab>  = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMainAdapter.ViewHolder {
        return ViewHolder(ItemRecommendTabBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = eventTabList[position].title
        var span: Spannable = holder.title.text as Spannable
        span.setSpan(ForegroundColorSpan(Color.parseColor(eventTabList[position].effectcolor)), eventTabList[position].effectrange.split(",")[0].toInt(), eventTabList[position].effectrange.split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), eventTabList[position].effectrange.split(",")[0].toInt(), eventTabList[position].effectrange.split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        (holder.recyclerView.adapter as RecommendRecipeAdapter).apply {
            setItemClickListener(object : RecommendRecipeAdapter.OnItemClickListener {
                override fun onClick(v: View, recipePosition: Int) {
                    itemCLickListener.onClick(eventTabList[holder.adapterPosition].recipelist[recipePosition])
                }
            })
            submitList(eventTabList[position].recipelist)
        }
    }

    override fun getItemCount(): Int {
        return eventTabList.size
    }

    fun submitList(eventTabList: MutableList<EventTab>) {
        this.eventTabList = eventTabList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemRecommendTabBinding) : RecyclerView.ViewHolder(binding.root) {
        internal var recyclerView : RecyclerView = binding.mainRecommendRecyclerview
        internal var title : TextView = binding.mainRecommendTitle
        init {
            recyclerView.apply {
                adapter = RecommendRecipeAdapter()
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }
    interface OnItemClickListener {
        fun onClick(data: Recipe)
    }
    fun setItemClickListener(onItemClickListener: SearchMainAdapter.OnItemClickListener) {
        this.itemCLickListener = onItemClickListener
    }
    private lateinit var itemCLickListener: SearchMainAdapter.OnItemClickListener
}
