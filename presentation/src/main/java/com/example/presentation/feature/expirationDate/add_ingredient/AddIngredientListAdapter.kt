package com.example.presentation.feature.expirationDate.add_ingredient

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.databinding.ItemAddIngredientBinding
import com.example.ingredient.feature.expirationDate.add_ingredient.models.Ingredient

class AddIngredientListAdapter(val view:AddIngredientsActivity): RecyclerView.Adapter<AddIngredientViewHolder>() {
    private var ingredients = ArrayList<Ingredient>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddIngredientViewHolder {
        return AddIngredientViewHolder(
            ItemAddIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddIngredientViewHolder, position: Int) {
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

// ViewPager
// 재료 카테고리별
// 전체 / 육류 / 채소 / ....  => 각 Fragment 생성
// 지금은 Activity에서 Viewpager를 이용하도록 만드는 과정