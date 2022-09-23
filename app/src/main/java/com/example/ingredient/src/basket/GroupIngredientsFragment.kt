package com.example.ingredient.src.basket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.R
import com.example.ingredient.databinding.FragmentGroupingredientsBinding
import com.example.ingredient.databinding.FragmentSearchBinding
import com.example.ingredient.src.basket.models.BasketIngredient

class GroupIngredientsFragment(basketList: ArrayList<BasketIngredient>): Fragment() {
    var basketList = basketList
    private var _binding : FragmentGroupingredientsBinding? = null
    private val binding get()  = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupingredientsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d("basketTest", "data Test : ${basketList}")
        val adapter = GroupIngredientsAdapter()
        val recyclerview = binding.groupRecyclerView
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.testmethod(basketList)
        // ViewPager 속 Recyclerview가 동작하지 않음.

    }
}