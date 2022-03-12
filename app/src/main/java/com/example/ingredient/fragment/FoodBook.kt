package com.example.ingredient.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.SearchAdapter
import com.example.ingredient.databinding.FragmentFoodBookBinding
import com.example.ingredient.databinding.FragmentSearchBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore

class FoodBook : Fragment() {
    private lateinit var adapter: SearchAdapter
    private lateinit var database: FirebaseFirestore
    private var strList = mutableListOf<String>()
    private var recipeList = mutableListOf<Array<Any>>()
    private var _binding : FragmentFoodBookBinding? = null
    private val binding get()  = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseFirestore.getInstance()
        _binding = FragmentFoodBookBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterConnect(recipeList)
    }

    override fun onStart() {
        super.onStart()
        SearchQuery(database)

        // 엔터키로 검색 실행 기능
        binding.findwindow.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchBtn.performClick()
                handled = true
            }
            handled
        }

        binding.searchBtn.setOnClickListener {
            var str = binding.findwindow.text.toString()
            binding.findwindow.setText("")
            if (!str.isNullOrBlank()) {
                SearchQuery(database, str)
            }
            else {
                SearchQuery(database)
            }
        }
    }
    // Default 쿼리문 (전체 출력)
    fun SearchQuery(database: FirebaseFirestore):Unit {
        val refs = database.collection("users")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList = mutableListOf<Array<Any>>()

        refs.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var int_str: String = document.get("ingredients").toString()
                    // 재료들을 포함하는 리스트
                    int_str = int_str.slice(1..int_str.lastIndex-1)
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장
                    recipeList.add(
                        arrayOf(
                            document.get("name").toString(),
                            int_str,
                            document.get("time").toString()
                        )
                    )
                }
                adapterConnect(recipeList)
            }
    }

    fun SearchQuery(database: FirebaseFirestore, str:String):Unit {
        val refs = database.collection("users")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList = mutableListOf<Array<Any>>()

        refs.whereGreaterThan("name", str)
            .whereLessThan("name", "$str\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var int_str: String = document.get("ingredients").toString()
                    // 재료들을 포함하는 리스트
                    int_str = int_str.slice(1..int_str.lastIndex-1)
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장
                    recipeList.add(
                        arrayOf(
                            document.get("name").toString(),
                            int_str,
                            document.get("time").toString()
                        )
                    )
                }
                adapterConnect(recipeList)
            }
    }

    private fun adapterConnect(recipeList: MutableList<Array<Any>>){
        adapter = SearchAdapter(recipeList)

        // Fragment
        adapter.setItemClickListener(object: SearchAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                PurchaseConfirmationDialogFragment(recipeList[position][0].toString()).show(
                    childFragmentManager, PurchaseConfirmationDialogFragment.TAG)
            }
        })

        binding.FindrecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}