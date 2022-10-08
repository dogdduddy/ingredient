package com.example.ingredient.src.basket.group.add_ingredient

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ingredient.src.expirationDate.add_ingredient.AddIngredientListFragment
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets

class AddGroupIngredientViewPagerAdapter (fa: FragmentActivity, val view: AddGroupIngredientsActivity):
    FragmentStateAdapter(fa){
    val TAG = "IngredientCategoryAdapter"
    private var ingredients = ArrayList<CategoryIngrediets>()

    override fun getItemCount(): Int = ingredients.size

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "IngredientCategoryAdapter - createFragment() : $position")
        val addgroupingredientListFragment = AddGroupIngredientListFragment(ingredients[position])
        return addgroupingredientListFragment
    }

    fun submitList(ingredients: ArrayList<CategoryIngrediets>) {
        this.ingredients = ingredients
        Log.d("adapter", "${this.ingredients}")
        notifyDataSetChanged()
    }
}