package com.example.ingredient.src.basket.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.databinding.FragmentGroupingredientsBinding
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        adapter.submitList(basketList)

        binding.groupAddBtn.setOnClickListener {
            binding.groupEditText.visibility = View.VISIBLE
            binding.AddComBtn.visibility = View.VISIBLE
        }

        binding.AddComBtn.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("ListData")
                .document(FirebaseAuth.getInstance().uid.toString())
                .collection("BasketList")
                .document()
                .set(hashMapOf("groupName" to binding.groupEditText.text.toString()))
            binding.groupEditText.visibility = View.GONE
            binding.AddComBtn.visibility = View.GONE
        }
    }
}