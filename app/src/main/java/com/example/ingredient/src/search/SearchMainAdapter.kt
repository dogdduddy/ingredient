package com.example.ingredient.src.search

import android.content.Context
import android.media.Image
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R

class SearchMainAdapter ()
    : RecyclerView.Adapter<SearchMainAdapter.ViewHolder>()
{
    private var recommendList: MutableList<ArrayList<String>>  = mutableListOf()
    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_main_recommend, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { itemCLickListener.onClick(it, position) }
        holder.image.apply {
            setImageResource(R.drawable.curry)
            clipToOutline = true
        }
        holder.title.text = recommendList[position][2]
        var span:Spannable = holder.title.text as Spannable
        span.setSpan(ForegroundColorSpan(context!!.getColor(R.color.orange_300)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.subtitle.text = recommendList[position][3]
    }

    override fun getItemCount(): Int {
        return recommendList.size
    }

    fun submitList(recommendList: MutableList<ArrayList<String>>) {
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
        fun onClick(view:View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemCLickListener = onItemClickListener
    }
    private lateinit var itemCLickListener: OnItemClickListener
}