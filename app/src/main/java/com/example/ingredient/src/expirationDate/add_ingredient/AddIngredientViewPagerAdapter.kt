package com.example.ingredient.src.expirationDate.add_ingredient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets

class AddIngredientViewPagerAdapter(fa:FragmentActivity, val view:AddIngredientsActivity):
FragmentStateAdapter(fa){
    val TAG = "IngredientCategoryAdapter"
    private var ingredients = ArrayList<CategoryIngrediets>()

    override fun getItemCount(): Int = ingredients.size

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "IngredientCategoryAdapter - createFragment() : $position")
        val addingredientListFragment = AddIngredientListFragment()
        addingredientListFragment.arguments = Bundle().apply {
            putParcelable("ingredients", ingredients[position])
        }
        return addingredientListFragment
    }

    fun submitList(ingredients: ArrayList<CategoryIngrediets>) {
        this.ingredients = ingredients
        Log.d("adapterTT", "${this.ingredients}")
        notifyDataSetChanged()
    }
}