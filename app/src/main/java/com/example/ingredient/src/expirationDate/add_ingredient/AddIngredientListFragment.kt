package com.example.ingredient.src.expirationDate.add_ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AddIngredientListFragment : Fragment() {
    private lateinit var addingredientListAdapter: AddIngredientListAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_ingredient_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*addingredientListAdapter = AddIngredientListAdapter(this)
        viewPager = view.findViewById(R.id.viewpager_add_ingredient)
        viewPager.adapter = addingredientListAdapter

         */

        val tabLayout:TabLayout = view.findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }
}