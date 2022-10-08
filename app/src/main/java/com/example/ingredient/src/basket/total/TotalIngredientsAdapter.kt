package com.example.ingredient.src.basket.total

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketGroupIngredient
import com.example.ingredient.src.basket.models.BasketIngredient

class TotalIngredientsAdapter():RecyclerView.Adapter<TotalIngredientsAdapter.ViewHolder>() {
    private var context: Context? = null
    private var ingredientList = arrayListOf<BasketGroupIngredient>()


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
        holder.Name.text = ingredientList[position].ingredientName
        holder.Quantity.text = ingredientList[position].quantity.toString()
    }

    override fun getItemCount(): Int = ingredientList.size

    fun submitList(basketList: ArrayList<BasketIngredient>){
        ingredientList.clear()
        basketList.forEach { basket ->
            if(ingredientList.map {it.ingredientName}.indexOf(basket.ingredientName) == -1){
                ingredientList.add(BasketGroupIngredient(basket.ingredientIcon, basket.ingredientIdx, basket.ingredientName, basket.categoryName, basket.quantity))
            }else{
                ingredientList[ingredientList.map {it.ingredientName}.indexOf(basket.ingredientName)]!!.quantity += basket.quantity
            }
        }
    }

}