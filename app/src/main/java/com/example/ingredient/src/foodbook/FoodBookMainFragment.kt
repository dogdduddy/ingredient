package com.example.ingredient.src.foodbook

import android.content.Context
import android.content.Intent
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
import com.example.ingredient.common.RecipeDialogActivity
import com.example.ingredient.databinding.FragmentFoodbookMainBinding
import com.example.ingredient.src.search.SearchAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodBookMainFragment : Fragment() {
    private var adapter = SearchAdapter()
    private lateinit var database: FirebaseFirestore
    private var _binding : FragmentFoodbookMainBinding? = null
    private var recipeList: ArrayList<MutableMap<String, String>> = ArrayList()
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

    private fun adapterConnect(recipeList: ArrayList<MutableMap<String, String>> = ArrayList()) {
        adapter.submitList(recipeList)

        // Fragment
        adapter.setItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", recipeList[position]["name"].toString())
                startActivity(intent)
            }
        })
        binding.FindrecyclerView.layoutManager = GridLayoutManager(activity, 2)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }

    fun query(position:Int) {
        val refs = database.collection("Recipes")
        recipeList.clear()

        refs.whereIn("name", categoryList[position][1] as List<Any>)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var ing_str: String = ""
                    (document.get("ingredient") as ArrayList<String>).forEachIndexed { index, element ->
                        if(index == 0)
                            ing_str = element
                        else
                            ing_str += " / $element"
                    }
                    recipeList.add(
                        mutableMapOf("name" to document.get("name").toString(),
                            "ingredient" to ing_str,
                            "like" to document.get("like").toString(),
                            "subscribe" to document.get("subscribe").toString()
                        ))
                }
                adapterConnect(recipeList)
            }
    }

    fun tabsInit(temp : ArrayList<ArrayList<Any>>) {
        categoryList.clear()
        categoryList.addAll(temp)
        Log.d("tabs",temp[0].toString())
        temp.forEach {
            tabs.addTab(tabs.newTab().setText(it[0].toString()))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FoodBookMainFragment()
    }

}