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

class SearchAdapter (private val recipeList: MutableList<Array<String>>)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>()
    {
        private var context:Context? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent!!.context).inflate(R.layout.item_recipe, parent, false)
            context = view.context
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipeList[position]
            Log.d("Data Test : ","$position, $recipe")
            /*
            when (position % 5) {
                0 -> holder.food.setImageResource(R.drawable.buckwheat)
                1 -> holder.food.setImageResource(R.drawable.bibim)
                2 -> holder.food.setImageResource(R.drawable.ham)
                3 -> holder.food.setImageResource(R.drawable.salad)
                4 -> holder.food.setImageResource(R.drawable.steak)
            }

             */
            holder.itemView.setOnClickListener {
                itemCLickListener.onClick(it, position)
            }
            holder!!.title.text = recipe[0].toString()
            holder.content.text = "재료 : " + recipe[1]
            holder.time.text = recipe[2].toString()
        }

        override fun getItemCount(): Int {
            return recipeList.size
        }

        inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var title: TextView
            internal var content: TextView
            internal var time: TextView
            internal var food: ImageView
            init {
                var context = context
            }
            init {
                title = view.findViewById(R.id.findTitle)
                content = view.findViewById(R.id.findContent)
                time = view.findViewById(R.id.findTime)
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