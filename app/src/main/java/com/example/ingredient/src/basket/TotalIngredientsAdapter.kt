package com.example.ingredient.src.basket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketIngredient

class TotalIngredientsAdapter():RecyclerView.Adapter<TotalIngredientsAdapter.ViewHolder>() {
    private var context: Context? = null
    private var basketList = arrayListOf<BasketIngredient>()
    private var recyclerView: RecyclerView? = null
    private var adapter:TotalIngredientsAdapter? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_total_ingredient, parent, false)
        context = view.context

        return ViewHolder(view)
    }
    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var Name : TextView
        internal var Quantity : TextView

        init {
            context = context
            Name = view.findViewById(R.id.total_ingredient_name)
            Quantity = view.findViewById(R.id.total_ingredient_quantity)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Name.text = basketList[position].ingredientName
        holder.Quantity.text = basketList[position].quantity.toString()
    }

    override fun getItemCount(): Int = basketList.size
    fun submitList(basketList: ArrayList<BasketIngredient>){
        this.basketList = basketList
    }

}