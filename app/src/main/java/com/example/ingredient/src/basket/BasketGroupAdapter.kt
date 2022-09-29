package com.example.ingredient.src.basket

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketGroupIngredient
import com.example.ingredient.src.basket.models.BasketIngredient

class BasketGroupAdapter: RecyclerView.Adapter<BasketGroupAdapter.ViewHolder>() {
    private var context: Context? = null
    private lateinit var DataList: ArrayList<BasketGroupIngredient>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketGroupAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_basketgroup, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount() = DataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientName.text = DataList[position].ingredientName
        holder.ingredientAmount.text = DataList[position].quantity.toString()
        Glide.with(holder.itemView)
            .load(DataList[position].ingredientIcon)
            .into(holder.ingredientIcon)

    }

    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var ingredientName : TextView
        internal var ingredientIcon : ImageView
        internal var ingredientAmount : TextView

        init {
            context = context
            ingredientName = view.findViewById(R.id.group_ingredientname)
            ingredientIcon = view.findViewById(R.id.group_ingredienticon)
            ingredientAmount = view.findViewById(R.id.group_ingredientamount)
        }
    }
    fun submitList(DataList: ArrayList<BasketGroupIngredient>){
        Log.d("submitList", "submitList: ${DataList}")
        this.DataList = DataList
    }
}