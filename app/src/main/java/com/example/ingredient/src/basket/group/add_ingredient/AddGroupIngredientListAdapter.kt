package com.example.ingredient.src.basket.group.add_ingredient

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.databinding.ItemAddGroupingredientBinding
import com.example.ingredient.databinding.ItemAddIngredientBinding
import com.example.ingredient.databinding.ItemGroupAddBinding
import com.example.ingredient.src.expirationDate.add_ingredient.AddIngredientViewHolder
import com.example.ingredient.src.expirationDate.add_ingredient.AddIngredientsActivity
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient

class AddGroupIngredientListAdapter(val view: AddGroupIngredientsActivity): RecyclerView.Adapter<AddGroupIngredientViewHolder>() {
    private var ingredients = ArrayList<Ingredient>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddGroupIngredientViewHolder {
        return AddGroupIngredientViewHolder(
            ItemAddGroupingredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddGroupIngredientViewHolder, position: Int) {
        Log.d("AdapterData", ingredients.toString())
        holder.bindWithView(ingredients[position])
        holder.itemView.setOnClickListener { view.addingredientClick(ingredients[position]) }
    }

    override fun getItemCount() = ingredients.size

    fun submitList(ingredients: ArrayList<Ingredient>?) {
        if(ingredients != null) {
            this.ingredients = ingredients
        }
        notifyDataSetChanged()
    }
}