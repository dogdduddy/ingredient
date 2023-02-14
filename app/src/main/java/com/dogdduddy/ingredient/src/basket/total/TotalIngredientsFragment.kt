package com.dogdduddy.ingredient.src.basket.total

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dogdduddy.ingredient.databinding.FragmentTotalingredientsBinding
import com.dogdduddy.ingredient.src.basket.BasketService
import com.dogdduddy.ingredient.src.basket.BasketView
import com.dogdduddy.ingredient.src.basket.models.BasketIngredient

class TotalIngredientsFragment: Fragment(), BasketView {
    private var _binding : FragmentTotalingredientsBinding? = null
    private val binding get()  = _binding!!
    private var basketList = ArrayList<BasketIngredient>()
    private var adapter = TotalIngredientsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTotalingredientsBinding.inflate(layoutInflater, container, false)
        var recyclerView = binding.totalRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.submitList(basketList)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        BasketService(this@TotalIngredientsFragment).getBasket()
    }

    fun submitList(basketData: ArrayList<BasketIngredient>) {
        this.basketList = basketData
        adapter.submitList(basketList)
    }

    override fun onGetBasketSuccess(response: ArrayList<BasketIngredient>) {
        submitList(response)
    }

    override fun onGetBasketFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun itemDeleteListener() {
        TODO("Not yet implemented")
    }

    override fun onGetBasketGroupSuccess(response: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun onGetBasketGroupFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostBasketGroupSuccess() {
        TODO("Not yet implemented")
    }

    override fun onPostBasketGroupFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupListSuccess(response: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupIngredientSuccess(response: String, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupListFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupIngredientFailure(message: String) {
        TODO("Not yet implemented")
    }
}