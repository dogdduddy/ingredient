package com.example.ingredient.feature.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.feature.foodbook.models.Recipe

class SearchAdapter ()
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>()
    {
        private val recipeList: ArrayList<Recipe> = arrayListOf()
        private var context:Context? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent!!.context).inflate(R.layout.item_recipe, parent, false)
            context = view.context
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipeList?.get(position)
            holder.itemView.setOnClickListener { itemCLickListener.onClick(it, position) }
            holder.like.text = recipe!!.like
            holder.subscribe.text = recipe!!.subscribe
            holder.title.text = recipe!!.name

            // 이미지 로드
            holder.food.clipToOutline = true
            Glide.with(holder.itemView)
                .load(recipe!!.icon)
                .into(holder.food)
        }

        override fun getItemCount(): Int {
            return recipeList!!.size
        }

        fun submitList(recipeList: List<Recipe>) {
            this.recipeList!!.clear()
            this.recipeList!!.addAll(recipeList)
            notifyDataSetChanged()
        }

        inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var title: TextView
            internal var food: ImageView
            internal var like: TextView
            internal var subscribe: TextView
            init {
                var context = context
            }
            init {
                title = view.findViewById(R.id.findTitle)
                food = view.findViewById(R.id.foodimg)
                like = view.findViewById(R.id.recipe_like)
                subscribe = view.findViewById(R.id.recipe_subscribe)
            }
        }
        // 마지막 chip 삭제했을 때는 값이 없으므로 쿼리가 불가능 => recyclerview 아이템 직접 삭제
        fun nullItem() {
            recipeList!!.removeAt((0))
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