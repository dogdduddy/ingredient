package com.example.presentation.feature.basket.group.addGroup.addIngredient

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.databinding.ItemAddGroupingredientBinding
import com.example.presentation.feature.expirationDate.add_ingredient.models.Ingredient

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