package com.example.ingredient.src.expirationDate.add_ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.R

class AddIngredientListAdapter(fragmentActivity: AddingredientsActivity):FragmentStateAdapter(fragmentActivity){
    val ARG_OBJECT = "object"
    val fragmentList = listOf<Fragment>(Fragment1(), Fragment2())
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}

// ViewPager
// 재료 카테고리별
// 전체 / 육류 / 채소 / ....  => 각 Fragment 생성


// 지금은 Activity에서 Viewpager를 이용하도록 만드는 과정