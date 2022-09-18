package com.example.ingredient.src.basket

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class basketViewPagerAdapter(fa: Fragment):FragmentStateAdapter(fa) {

    var fragments:ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun setFragment(fragments:ArrayList<Fragment>) {
        this.fragments.addAll(fragments)
        notifyDataSetChanged()
    }
}
