package com.dogdduddy.ingredient.src.foodbook

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
import com.dogdduddy.ingredient.R
import com.dogdduddy.ingredient.activity.MainActivity
import com.dogdduddy.ingredient.common.RecipeDialogActivity
import com.dogdduddy.ingredient.databinding.FragmentFoodbookMainBinding
import com.dogdduddy.ingredient.src.foodbook.models.FoodCategory
import com.dogdduddy.ingredient.src.foodbook.models.Recipe
import com.dogdduddy.ingredient.src.search.SearchAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodBookMainFragment : Fragment() {
    private var adapter = SearchAdapter()
    private lateinit var database: FirebaseFirestore
    private var _binding : FragmentFoodbookMainBinding? = null
    private var recipeList: ArrayList<Recipe> = arrayListOf()
    private val binding get()  = _binding!!
    private var imm: InputMethodManager? = null
    private var categoryList = arrayListOf<FoodCategory>()
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
            var CategoryDataList = arrayListOf<FoodCategory>()
            for (document in result) {
                CategoryDataList.add(document.toObject<FoodCategory>())
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
                adapterConnect(categoryList[tab?.position!!].recipes!!)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO()
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO()
            }
        })

    }

    private fun adapterConnect(recipeList: List<Recipe>) {
        adapter.submitList(recipeList)
        // Fragment
        adapter.setItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                Log.d("searchTest", "${recipeList[position].name}  : ${recipeList[position].ingredient}")
                intent.putExtra("name", recipeList[position].name)
                (activity as MainActivity).addRecentRecipe(recipeList[position].name, recipeList[position].icon)
                startActivity(intent)
            }
        })
        binding.FindrecyclerView.layoutManager = GridLayoutManager(activity, 2)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }

    fun tabsInit(temp : ArrayList<FoodCategory>) {
        categoryList.clear()
        categoryList.addAll(temp.sortedBy { it.categoryid })
        categoryList.forEach {
            tabs.addTab(tabs.newTab().setText(it.categoryname))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FoodBookMainFragment()
    }

}