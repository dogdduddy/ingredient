package com.example.ingredient.src.basket.total

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.databinding.FragmentBasketBinding
import com.example.ingredient.databinding.FragmentTotalingredientsBinding
import com.example.ingredient.src.basket.BasketService
import com.example.ingredient.src.basket.BasketView
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TotalIngredientsFragment: Fragment(), BasketView {
    private var _binding : FragmentTotalingredientsBinding? = null
    private val binding get()  = _binding!!
    private var basketList = ArrayList<BasketIngredient>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTotalingredientsBinding.inflate(layoutInflater, container, false)
        var recyclerView = binding.totalRecyclerview
        var adapter = TotalIngredientsAdapter()
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

    /*
    fun dataInit() {
        val userid = FirebaseAuth.getInstance().uid!!
        val database = FirebaseFirestore.getInstance()
        database.collection("ListData")
            .document(userid!!)
            .collection("Basket")
            .get()
            .addOnSuccessListener { documents ->
                basketList.clear()
                for(document in documents) {
                    basketList.add(BasketIngredient(
                        document.get("ingredienticon").toString(),
                        document.get("ingredientidx").toString().toInt(),
                        document.get("ingredientname").toString(),
                        document.get("ingredientcategory").toString(),
                        document.get("groupName").toString(),
                        document.get("ingredientquantity").toString().toInt()
                    ))
                }
            }
        submitList(basketList)
    }

     */

    fun submitList(basketData: ArrayList<BasketIngredient>) {
        this.basketList = basketData
    }

    override fun onGetFridgeSuccess(response: ArrayList<BasketIngredient>) {
        submitList(response)
    }

    override fun onGetFridgeFailure(message: String) {
        TODO("Not yet implemented")
    }
}