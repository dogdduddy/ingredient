package com.example.ingredient.src.basket.total

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketIngredient

class TotalIngredientsFragment: Fragment() {
    private var basketList: ArrayList<BasketIngredient>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_totalingredients, container, false)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.total_recyclerview)!!
        var adapter = TotalIngredientsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.submitList(basketList!!)
        return view
    }

    fun submitList(basketData: ArrayList<BasketIngredient>) {
        this.basketList = basketData
    }
}