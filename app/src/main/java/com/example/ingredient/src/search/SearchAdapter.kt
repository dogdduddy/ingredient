package com.example.ingredient.src.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R

class SearchAdapter ()
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>()
    {
        private val recipeList: MutableList<Array<String>>  = mutableListOf()
        private var context:Context? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent!!.context).inflate(R.layout.item_recipe, parent, false)
            context = view.context
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipeList[position]
            holder.itemView.setOnClickListener { itemCLickListener.onClick(it, position) }
            holder.food.apply {
                setImageResource(R.drawable.curry)
                clipToOutline = true
            }
            holder!!.title.text = recipe[0]
        }

        override fun getItemCount(): Int {
            return recipeList.size
        }

        fun submitList(recipeList: MutableList<Array<String>>) {
            this.recipeList.clear()
            this.recipeList.addAll(recipeList)
            notifyDataSetChanged()
        }

        inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var title: TextView
            internal var food: ImageView
            init {
                var context = context
            }
            init {
                title = view.findViewById(R.id.findTitle)
                food = view.findViewById(R.id.foodimg)

            }
        }
        // 마지막 chip 삭제했을 때는 값이 없으므로 쿼리가 불가능 => recyclerview 아이템 직접 삭제
        fun nullItem() {
            recipeList.removeAt((0))
            notifyItemRemoved(0)
            notifyDataSetChanged()
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