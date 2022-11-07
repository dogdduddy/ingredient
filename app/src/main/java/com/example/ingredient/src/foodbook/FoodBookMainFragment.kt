package com.example.ingredient.src.foodbook

import android.content.Context
import android.os.Bundle
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

class FoodBookMainFragment : Fragment() {
    private var adapter = SearchAdapter()
    private lateinit var database: FirebaseFirestore
    private var _binding : FragmentFoodbookMainBinding? = null
    private val binding get()  = _binding!!
    private var imm: InputMethodManager? = null

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
        // 원래는 전체
        query("김치볶음밥")


        var tabs:TabLayout = binding.foodbookMainTablayout
        tabs.addTab(tabs.newTab().setText("전체"))
        tabs.addTab(tabs.newTab().setText("한식"))
        tabs.addTab(tabs.newTab().setText("중식"))
        tabs.addTab(tabs.newTab().setText("양식"))

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var category = ""
                var inputData = ""
                when(tab?.position){
                    0 -> category = "전체"
                    1 -> category = "한식"
                    2 -> category = "중식"
                    3 -> category = "양식"
                }
                // 해당 카테고리의 레시피 리스트를 받을 수 있도록 쿼리문 짜기
                when(category) {
                    "전체" -> inputData = "김치볶음밥"
                    "한식" -> inputData = "김치전"
                    "중식" -> inputData = "비빔밥"
                    "양식" -> inputData = "호박전"
                }
                query(inputData)
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

    fun query(inputData:String) {
        val refs = database.collection("Recipes")
        // 검색 통해 나온 레시피명을 담는 리스트
        var recipeList = mutableListOf<Array<String>>()
        refs.whereArrayContains("fulltext", inputData)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장
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

    companion object {
        @JvmStatic
        fun newInstance() = FoodBookMainFragment()
    }

}