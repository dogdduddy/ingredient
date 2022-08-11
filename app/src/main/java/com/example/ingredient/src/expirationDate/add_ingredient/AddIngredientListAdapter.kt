package com.example.ingredient.src.expirationDate.add_ingredient

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ingredient.databinding.ItemIngredientBinding
import com.example.ingredient.databinding.ItemRecipeBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient

class AddIngredientListAdapter(val view:AddIngredientsActivity): RecyclerView.Adapter<AddIngredientViewHolder>() {
    private var ingredients = ArrayList<Ingredient>()
    private var clickableCheck = ArrayList<Boolean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddIngredientViewHolder {
        return AddIngredientViewHolder(
            ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddIngredientViewHolder, position: Int) {
        Log.d("AdapterData", ingredients.toString())
        holder.bindWithView(ingredients[position])
        holder.itemView.isClickable = clickableCheck[position]
        holder.itemView.setOnClickListener {
            holder.itemView.isClickable = false
            clickableCheck[position] = false
            view.addingredientClick(ingredients[position].ingredientName)
        }
    }

    override fun getItemCount() = ingredients.size

    fun submitList(ingredients: ArrayList<Ingredient>?) {
        if(ingredients != null) {
            this.ingredients = ingredients
            clickableCheck = ArrayList<Boolean>(ingredients.size)
            (0 until ingredients.size).forEach {clickableCheck.add(true)}
        }
        notifyDataSetChanged()
    }

    fun changedData(position:Int) {
        clickableCheck[position] = true
        notifyItemChanged(position)
    }
}

// ViewPager
// 재료 카테고리별
// 전체 / 육류 / 채소 / ....  => 각 Fragment 생성
// 지금은 Activity에서 Viewpager를 이용하도록 만드는 과정