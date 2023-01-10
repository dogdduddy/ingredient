package com.example.ingredient.src.search

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.R
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.common.RecipeDialogActivity
import com.example.ingredient.databinding.FragmentMainBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.foodbook.models.EventTab
import com.example.ingredient.src.foodbook.models.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SearchMainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get()  = _binding!!
    private var adapter1 = SearchMainAdapter()
    private var adapter2 = SearchMainAdapter()
    private var adapter3 = SearchMainAdapter()
    private lateinit var temp1:MutableList<Recipe>
    private lateinit var temp2:MutableList<Recipe>
    private lateinit var temp3:MutableList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            async {  persnalRecommendInit() }.await()
            async {  recommendInit() }.await()
            submitList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        var recyclerView1 = binding.mainRecommendRecyclerview1
        var recyclerView2 = binding.mainRecommendRecyclerview2
        var recyclerView3 = binding.mainRecommendRecyclerview3

        recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerView1.itemAnimator = DefaultItemAnimator()
        recyclerView2.itemAnimator = DefaultItemAnimator()
        recyclerView3.itemAnimator = DefaultItemAnimator()

        recyclerView1.adapter = adapter1
        recyclerView2.adapter = adapter2
        recyclerView3.adapter = adapter3

       return binding.root
    }

    fun submitList() {
        adapter1.submitList(temp1!!)
        adapter2.submitList(temp2)
        adapter3.submitList(temp3)
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            async {  persnalRecommendInit() }.await()
            async {  recommendInit() }.await()
            submitList()
        }

        binding.mainSearchbar.setOnClickListener {
            val searchFragment = SearchFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.add(R.id.fragment_container, searchFragment)
            transaction?.commit()
        }
        adapter1.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp1!![position].name)
                (activity as MainActivity).addRecentRecipe(temp1!![position].name, temp1!![position].icon)
                startActivity(intent)
            }
        })
        adapter2.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp2[position].name)
                (activity as MainActivity).addRecentRecipe(temp2[position].name, temp2[position].icon)
                startActivity(intent)
            }
        })
        adapter3.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp3[position].name)
                (activity as MainActivity).addRecentRecipe(temp3[position].name, temp3[position].icon)
                startActivity(intent)
            }
        })

    }
    suspend fun persnalRecommendInit() {
        // recommend List
        var middle = mutableListOf<Recipe>()
        var database = FirebaseFirestore.getInstance()
        var result = arrayListOf<ArrayList<Int>>()
        var responseR = database.collection("Recipes").get()
            .await().toObjects<Recipe>()
        var responseRMap = responseR.map { it.ingredient }

        var responseM = database.collection("ListData")
            .document(FirebaseAuth.getInstance().uid!!)
            .collection("Refrigerator")
            .get().await().toMutableList().map{it.data["ingredientname"]}.toSet()

        responseRMap.forEachIndexed { index, doc ->
            result.add(arrayListOf(index, doc!!.toSet().intersect(responseM).size))
        }

        var sortedResult = result.sortedBy { it[1] }.reversed()
        temp1 = mutableListOf(responseR[sortedResult[0][0]], responseR[sortedResult[1][0]], responseR[sortedResult[2][0]])

        binding.mainRecommendTitle1.text = "재료로 보는 추천 레시피"
        var span1: Spannable = binding.mainRecommendTitle1.text as Spannable
        span1.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.orange_300)), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span1.setSpan(StyleSpan(Typeface.BOLD), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    }
    suspend fun recommendInit() {
        val response = FirebaseFirestore.getInstance().collection("Event").get()
            .await().toObjects<EventTab>().sortedBy { it.eventidx }

        var temp2Response = response[0]
        var temp3Response = response[1]

        temp2 = temp2Response.recipelist
        temp3 = temp3Response.recipelist

        binding.mainRecommendTitle2.text = temp2Response.title
        binding.mainRecommendTitle3.text = temp3Response.title

        var span2: Spannable = binding.mainRecommendTitle2.text as Spannable
        var span3: Spannable = binding.mainRecommendTitle3.text as Spannable

        span2.setSpan(ForegroundColorSpan(Color.parseColor(temp2Response.effectcolor)), temp2Response.effectrange.split(",")[0].toInt(), temp2Response.effectrange.split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span2.setSpan(StyleSpan(Typeface.BOLD), temp2Response.effectrange.split(",")[0].toInt(), temp2Response.effectrange.split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(ForegroundColorSpan(Color.parseColor(temp3Response.effectcolor)), temp3Response.effectrange.split(",")[0].toInt(), temp3Response.effectrange.split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(StyleSpan(Typeface.BOLD), temp3Response.effectrange.split(",")[0].toInt(), temp3Response.effectrange.split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    companion object {
        fun newInstance() =
            SearchMainFragment()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}