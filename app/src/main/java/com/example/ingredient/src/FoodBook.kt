package com.example.ingredient.src

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.common.PurchaseConfirmationDialogFragment
import com.example.ingredient.data.ExpirationDateDatabase
import com.example.ingredient.databinding.FragmentFoodBookBinding
import com.example.ingredient.src.search.SearchAdapter
import com.google.firebase.firestore.FirebaseFirestore

class FoodBook : Fragment() {
    private lateinit var adapter: SearchAdapter
    private lateinit var database: FirebaseFirestore
    private lateinit var db: ExpirationDateDatabase

    private var recipeList = mutableListOf<Array<String>>()
    private var _binding: FragmentFoodBookBinding? = null
    private val binding get() = _binding!!
    private val KEY_DATA = "KEY_DATA"
    private var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
        if (savedInstanceState != null) {
            var data = savedInstanceState.getBundle("Key")
            recipeList = data

        }

 */
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseFirestore.getInstance()
        _binding = FragmentFoodBookBinding.inflate(layoutInflater, container, false)
        Log.d("null test", binding.findwindow.text.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterConnect(recipeList)
    }

    override fun onStart() {
        super.onStart()
        /*
        // 22.03.15  ?????? ?????????????????? ?????? ??? ???????????? ?????? ?????? ???????????? ??????
        if(binding.findwindow.text.toString().isNullOrBlank())  SearchQuery(database)
        else SearchQuery(database, binding.findwindow.text.toString())
         */

        // ???????????? ?????? ?????? ??????
        binding.findwindow.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchBtn.performClick()
                handled = true
            }
            handled
        }

        binding.searchBtn.setOnClickListener {
            // ?????? ??? ????????? ?????????
            imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(binding.findwindow.windowToken, 0)

            var str = binding.findwindow.text.toString()
            if (!str.isNullOrBlank()) {
                SearchQuery(database, str)
            } else {
                SearchQuery(database)
            }
        }

    }


    // ??????????????? (?????? ??????)
    fun SearchQuery(database: FirebaseFirestore): Unit {
        val refs = database.collection("users")
        // ?????? ?????? ?????? ??????????????? ?????? ?????????
        recipeList = mutableListOf<Array<String>>()

        refs.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // ????????? ???????????? ?????? ??????, ??????, ?????? ??????
                    recipeList.add(
                        arrayOf(
                            document.get("name").toString(),
                            // ["??????", ???, "??????"] ??? ?????? ????????? ?????? ???. "[" ??? "]"??? ???????????? ?????? ??????
                            document.get("ingredients").toString().drop(1).dropLast(1),
                            document.get("time").toString()
                        )
                    )
                }
                adapterConnect(recipeList)
            }
    }

    // ??????????????? (?????? ???????????? ?????? ????????? ??????)
    fun SearchQuery(database: FirebaseFirestore, str: String): Unit {
        val refs = database.collection("users")
        // ?????? ?????? ?????? ??????????????? ?????? ?????????
        recipeList = mutableListOf<Array<String>>()

        refs.whereGreaterThan("name", str)
            .whereLessThan("name", "$str\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // ????????? ???????????? ?????? ??????, ??????, ?????? ??????
                    recipeList.add(
                        arrayOf(
                            document.get("name").toString(),
                            document.get("ingredients").toString().drop(1).dropLast(1),
                            document.get("time").toString()
                        )
                    )
                }
                adapterConnect(recipeList)
            }
    }

    private fun adapterConnect(recipeList: MutableList<Array<String>>) {
        adapter = SearchAdapter(recipeList)

        // Fragment
        adapter.setItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                PurchaseConfirmationDialogFragment(recipeList[position][0].toString()).show(
                    childFragmentManager, PurchaseConfirmationDialogFragment.TAG
                )
            }
        })
        binding.FindrecyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }
    companion object {
        fun newInstance(db: ExpirationDateDatabase) =
            FoodBook().apply {
                this.db = db
            }
    }
}

