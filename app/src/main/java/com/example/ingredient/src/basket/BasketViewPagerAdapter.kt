package com.example.ingredient.src.basket

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ingredient.src.basket.group.GroupIngredientsFragment
import com.example.ingredient.src.basket.models.BasketIngredient
import com.example.ingredient.src.basket.total.TotalIngredientsFragment

class BasketViewPagerAdapter(fa: Fragment):FragmentStateAdapter(fa) {
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
        Log.d("categoryTest", "BasketViewPagerAdapter : ${basketList[0].ingredientName}  /  ${basketList[0].categoryName}")
        fragments = arrayListOf(GroupIngredientsFragment(), TotalIngredientsFragment())
        fragments.forEach { fragment ->
            when(fragment) {
                is GroupIngredientsFragment -> fragment.submitList(basketList)
                is TotalIngredientsFragment -> fragment.submitList(basketList)
            }
        }
        fmIds = fragments.map { it.hashCode().toLong() }
        notifyDataSetChanged()
    }
}
