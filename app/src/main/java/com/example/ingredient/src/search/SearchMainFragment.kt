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
    private var eventTabList: MutableList<EventTab> = mutableListOf()
    private var adapter:SearchMainAdapter = SearchMainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        adapter.setItemClickListener(object : SearchMainAdapter.OnItemClickListener{
            override fun onClick(data: Recipe) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", data.name)
                (activity as MainActivity).addRecentRecipe(data.name, data.icon)
                startActivity(intent)
            }
        })
        binding.mainRecommendRecyclerview.adapter = adapter
        binding.mainRecommendRecyclerview.layoutManager = LinearLayoutManager(context)

       return binding.root
    }

    fun submitList() {
        adapter.submitList(eventTabList)
    }

    override fun onStart() {
        super.onStart()
        eventTabList.clear()
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
        var temp1 = mutableListOf(responseR[sortedResult[0][0]], responseR[sortedResult[1][0]], responseR[sortedResult[2][0]])

        eventTabList.add(EventTab(
            title="재료로 보는 추천 레시피",
            effectcolor = "#" + Integer.toHexString(R.color.orange_300),
            effectrange = "7,9",
            effectstyle = "bold",
            eventidx = "0",
            recipelist = temp1))
    }
    suspend fun recommendInit() {
        val response = FirebaseFirestore.getInstance().collection("Event").get()
            .await().toObjects<EventTab>().sortedBy { it.eventidx }
        eventTabList.addAll(response)
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