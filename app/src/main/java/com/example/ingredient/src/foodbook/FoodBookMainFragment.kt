package com.example.ingredient.src.foodbook

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ingredient.R
import com.example.ingredient.common.PurchaseConfirmationDialogFragment
import com.example.ingredient.databinding.FragmentFoodbookMainBinding
import com.example.ingredient.databinding.FragmentMainBinding
import com.example.ingredient.src.search.SearchAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodBookMainFragment : Fragment() {
    private var adapter = SearchAdapter()
    private lateinit var database: FirebaseFirestore
    private var _binding : FragmentFoodbookMainBinding? = null
    private val binding get()  = _binding!!
    private var imm: InputMethodManager? = null
    private var categoryList = arrayListOf<ArrayList<Any>>()
    private lateinit var tabs:TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        database = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodbookMainBinding.inflate(layoutInflater, container, false)
        tabs = binding.foodbookMainTablayout
        database.collection("FoodCategory").get().addOnSuccessListener { result ->
            var CategoryDataList = arrayListOf<ArrayList<Any>>()
            for (document in result) {
                CategoryDataList.add(arrayListOf(document.data["categoryname"].toString(), document.data["recipes"] as ArrayList<Any>))
            }
            tabsInit(CategoryDataList)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.foodbookMainSearchbar.setOnClickListener {
            val foodBookFragment = FoodBookFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.add(R.id.fragment_container, foodBookFragment)
            transaction?.commit()
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                query(tab?.position!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //query("김치볶음밥")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //
            }
        })

    }

    private fun adapterConnect(recipeList: MutableList<Array<String>>) {
        adapter.submitList(recipeList)

        // Fragment
        adapter.setItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                PurchaseConfirmationDialogFragment(recipeList[position][0].toString()).show(
                    childFragmentManager, PurchaseConfirmationDialogFragment.TAG
                )
            }
        })
        binding.FindrecyclerView.layoutManager = GridLayoutManager(activity, 2)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }

    fun query(position:Int) {
        val refs = database.collection("Recipes")
        var recipeList = mutableListOf<Array<String>>()

        refs.whereIn("name", categoryList[position][1] as List<Any>)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
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

    fun tabsInit(temp : ArrayList<ArrayList<Any>>) {
        categoryList.addAll(temp)
        temp.forEach {
            tabs.addTab(tabs.newTab().setText(it[0].toString()))
        }
        query(0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FoodBookMainFragment()
    }

}