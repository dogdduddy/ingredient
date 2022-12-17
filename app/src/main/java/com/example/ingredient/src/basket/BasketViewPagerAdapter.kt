package com.example.ingredient.src.basket

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ingredient.src.basket.group.GroupIngredientsFragment
import com.example.ingredient.src.basket.models.BasketIngredient
import com.example.ingredient.src.basket.total.TotalIngredientsFragment

class BasketViewPagerAdapter(fa: Fragment, listener: BasketView):FragmentStateAdapter(fa) {
    private lateinit var basketList:ArrayList<BasketIngredient>
    private lateinit var fmIds:List<Long>
    private val listener: BasketView = listener
    private var fragments = arrayListOf<Fragment>(GroupIngredientsFragment(), TotalIngredientsFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
    override fun getItemId(position: Int): Long = fragments[position].hashCode().toLong()
    override fun containsItem(itemId: Long): Boolean = fmIds.contains(itemId)

    fun submitList(basketList: ArrayList<BasketIngredient>) {
        this.basketList = basketList
        fragments.forEach { fragment ->
            when(fragment) {
                is GroupIngredientsFragment -> fragment.submitList(basketList, listener)
                is TotalIngredientsFragment -> fragment.submitList(basketList)
            }
        }
        fmIds = fragments.map { it.hashCode().toLong() }
        notifyDataSetChanged()
    }

    fun deleteItem() {
        Log.d("testT", "groupRemove")
        notifyItemChanged(1)
    }
}
