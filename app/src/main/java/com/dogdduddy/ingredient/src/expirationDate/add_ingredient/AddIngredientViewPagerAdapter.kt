package com.dogdduddy.ingredient.src.expirationDate.add_ingredient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dogdduddy.ingredient.src.basket.group.addGroup.addIngredient.AddGroupIngredientListFragment
import com.dogdduddy.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets

class AddIngredientViewPagerAdapter(fa:FragmentActivity, val view:AddIngredientsActivity):
FragmentStateAdapter(fa){
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
            fmList.add(AddIngredientListFragment())
        Log.d("adapterT", "${this.ingredients}")
        notifyDataSetChanged()
    }
}