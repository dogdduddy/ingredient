package com.example.ingredient.src.basket

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ingredient.src.basket.models.BasketIngredient

class basketViewPagerAdapter(fa: Fragment):FragmentStateAdapter(fa) {
    private lateinit var basketList:ArrayList<BasketIngredient>
    private lateinit var fmIds:List<Long>
    var fragments = arrayListOf<Fragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
    override fun getItemId(position: Int): Long = fragments[position].hashCode().toLong()
    override fun containsItem(itemId: Long): Boolean = fmIds.contains(itemId)

    fun submitList(basketList: ArrayList<BasketIngredient>) {
        this.basketList = basketList
        fragments = arrayListOf(GroupIngredientsFragment(basketList), TotalIngredientsFragment(basketList))
        fmIds = fragments.map { it.hashCode().toLong() }
        notifyDataSetChanged()
    }


}
