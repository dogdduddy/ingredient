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
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TotalIngredientsFragment: Fragment() {
    private var basketList = ArrayList<BasketIngredient>()

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

        adapter.submitList(basketList)
        return view
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycler", "onResume")
        dataInit()
    }

    override fun onStart() {
        super.onStart()

        Log.d("lifecycler", "onStart")
    }

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

    fun submitList(basketData: ArrayList<BasketIngredient>) {
        this.basketList = basketData
    }
}