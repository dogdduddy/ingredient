package com.example.ingredient.src.foodbook

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ingredient.R
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.common.RecipeDialogActivity
import com.example.ingredient.database.ExpirationDateDatabase
import com.example.ingredient.databinding.FragmentFoodBookBinding
import com.example.ingredient.src.foodbook.models.Recipe
import com.example.ingredient.src.search.SearchAdapter
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FoodBookFragment : Fragment() {
    private var adapter = SearchAdapter()
    private lateinit var database: FirebaseFirestore
    private var strList = mutableListOf<String>()
    private var maxStrListSize = 8

    private var recipeList: ArrayList<Recipe> = arrayListOf()
    private var _binding: FragmentFoodBookBinding? = null
    private val binding get() = _binding!!
    private var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

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
    }

    override fun onStart() {
        super.onStart()
        IniteditTextFocus()
        /*
        // 22.03.15  다른 프래그먼트로 전환 후 복귀해도 검색 쿼리 유지되는 기능
        if(binding.findwindow.text.toString().isNullOrBlank())  SearchQuery(database)
        else SearchQuery(database, binding.findwindow.text.toString())
         */

        // 엔터키로 검색 실행 기능
        binding.findwindow.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchBtn.performClick()
                handled = true
            }
            handled
        }

        binding.searchCancleBtn.setOnClickListener {
            binding.findwindow.setText("")
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        binding.searchBtn.setOnClickListener {
            // 검색 후 키보드 내리기
            hideKeyboard()

            var inputData = binding.findwindow.text.toString().trim()
            if (!inputData.isNullOrBlank()) {
                hideKeyboard()
                if(checkDuplicate(inputData)) {
                    SearchFullTextQuery(database, inputData, false)
                }
                binding.findwindow.setText("")
            } else {
                SearchQuery(database)
            }
        }


    }

    // 단순쿼리문 (전체 출력)
    fun SearchQuery(database: FirebaseFirestore): Unit {
        val refs = database.collection("Recipes")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList.clear()

        refs.get()
            .addOnSuccessListener { documents ->
                binding.FindrecyclerView.visibility = View.VISIBLE
                binding.foodbookNotfoundLayout.visibility = View.GONE
                for (document in documents) {
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장
                    var ing_str: String = ""
                    (document.get("ingredient") as ArrayList<String>).forEachIndexed { index, element ->
                        if(index == 0)
                            ing_str = element
                        else
                            ing_str += " / $element"
                    }
                    recipeList.add(document.toObject<Recipe>())
                }
                adapterConnect(recipeList)
            }

    }

    // Full Text 형태로 검색 구현
    fun SearchFullTextQuery(database: FirebaseFirestore, str: String, chipClick:Boolean): Unit {
        val refs = database.collection("Recipes")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList.clear()

        refs.whereArrayContains("fulltext", str)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    showNotFound(str)
                } else {
                    hideNotFound()
                    for (document in documents) {
                        // 레시피 검색해서 나온 이름, 재료, 시간 저장
                        var ing_str: String = ""
                        (document.get("ingredient") as ArrayList<String>).forEachIndexed { index, element ->
                            if(index == 0)
                                ing_str = element
                            else
                                ing_str += " / $element"
                        }
                        recipeList.add(document.toObject<Recipe>())
                    }
                    if(!chipClick) {
                        addChip(str)
                    }
                    adapterConnect(recipeList)
                }
            }
    }

    // 단순쿼리문 (검색 키워드에 속한 결과만 출력)
    fun SearchQuery(database: FirebaseFirestore, str: String): Unit {
        val refs = database.collection("users")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList.clear()

        refs.whereGreaterThan("name", str)
            .whereLessThan("name", "$str\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장
                    var ing_str: String = ""
                    (document.get("ingredient") as ArrayList<String>).forEachIndexed { index, element ->
                        if(index == 0)
                            ing_str = element
                        else
                            ing_str += " / $element"
                    }
                    recipeList.add(document.toObject<Recipe>())
                }
                adapterConnect(recipeList)
            }
    }

    private fun adapterConnect(recipeList: ArrayList<Recipe>) {
        adapter.submitList(recipeList)

        // Fragment
        adapter.setItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", recipeList[position].name)
                (activity as MainActivity).addRecentRecipe(recipeList[position].name, recipeList[position].icon)
                startActivity(intent)
            }
        })
        binding.FindrecyclerView.layoutManager = GridLayoutManager(activity, 2)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }

    fun addChip(inputData:String) {
        strList.add(0,inputData)
        if (strList.size > maxStrListSize) {
            strList.removeLast()
            binding.chipGroup.removeViewAt(maxStrListSize-1)
        }
        binding.chipGroup.addView(Chip(context).apply {
            text = inputData // chip 텍스트 설정
            setOnClickListener {
                binding.findwindow.setText(text)
                SearchFullTextQuery(database, text.toString(), true)
            }
        }, 0)
    }

    // 검색 후 키보드 내리기
    fun hideKeyboard() {
        imm?.hideSoftInputFromWindow(binding.findwindow.windowToken,0)
    }

    fun showKeyboard() {
        imm?.showSoftInput(binding.findwindow,0)
    }

    fun IniteditTextFocus() {
        binding.findwindow.requestFocus()
        showKeyboard()
    }


    fun checkDuplicate(element: String): Boolean {
        return !strList.any { it == element.trim()}
    }

    fun showNotFound(str:String) {
        binding.foodbookNotfoundLayout.visibility = View.VISIBLE
        binding.FindrecyclerView.visibility = View.GONE
        binding.foodbookNotfoundText.text = str + " 레시피를 찾을 수가 없었어요.."
        var span:Spannable = binding.foodbookNotfoundText.text as Spannable
        span.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.orange_300)), 0, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun hideNotFound() {
        binding.FindrecyclerView.visibility = View.VISIBLE
        binding.foodbookNotfoundLayout.visibility = View.GONE
    }

    companion object {
        fun newInstance() =
            FoodBookFragment()
    }
}

