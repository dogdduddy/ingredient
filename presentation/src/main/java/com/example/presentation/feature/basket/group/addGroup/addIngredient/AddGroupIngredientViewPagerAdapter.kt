package com.example.presentation.feature.basket.group.addGroup.addIngredient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ingredient.feature.expirationDate.add_ingredient.models.CategoryIngrediets

class AddGroupIngredientViewPagerAdapter (fa: FragmentActivity, val view: AddGroupIngredientsActivity):
    FragmentStateAdapter(fa){
    val TAG = "IngredientCategoryAdapter"
    private var ingredients = ArrayList<CategoryIngrediets>()
    private var fmList: ArrayList<Fragment> = ArrayList()
    private val fmIds = fmList.map { it.hashCode().toLong() }

    override fun getItemCount(): Int = ingredients.size

    override fun createFragment(position: Int): Fragment {
        fmList[position].arguments = Bundle().apply {
            putParcelable("ingredients", ingredients[position])
        }
        return fmList[position]
    }

    override fun containsItem(itemId: Long): Boolean {
        return fmIds.contains(itemId)
    }

    override fun getItemId(position: Int): Long {
        return fmList[position].hashCode().toLong()
    }
    fun submitList(ingredients: ArrayList<CategoryIngrediets>) {
        this.ingredients = ingredients
        fmList.clear()
        for (i in 0 until (ingredients.size))
            fmList.add(AddGroupIngredientListFragment())
        Log.d("adapterT", "${this.ingredients}")
        notifyDataSetChanged()
    }
}