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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SearchMainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get()  = _binding!!
    private var adapter1 = SearchMainAdapter()
    private var adapter2 = SearchMainAdapter()
    private var adapter3 = SearchMainAdapter()
    private lateinit var temp1:MutableList<ArrayList<String>>
    private lateinit var temp2:MutableList<ArrayList<String>>
    private lateinit var temp3:MutableList<ArrayList<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            temp1 = async {  persnalRecommendInit() }.await()
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

        /*
        binding.mainRecommendTitle1.text = "재료로 보는 추천 레시피"
        binding.mainRecommendTitle2.text = "집이 곧 카페가 되는 홈베이킹 레시피"
        //binding.mainRecommendTitle2.text = data.get(0).get("title").toString()
        binding.mainRecommendTitle3.text = "추운 겨울을 따뜻하게 보낼 레시피"
        var span1: Spannable = binding.mainRecommendTitle1.text as Spannable
        var span2: Spannable = binding.mainRecommendTitle2.text as Spannable
        var span3: Spannable = binding.mainRecommendTitle3.text as Spannable

        //span1.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.orange_300)), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //span1.setSpan(StyleSpan(Typeface.BOLD), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        span2.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.apricot)), 12, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span2.setSpan(StyleSpan(Typeface.BOLD), 12, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //span2.setSpan(ForegroundColorSpan(Color.parseColor(data[0].get("effectcolor").toString())), data[0].get("effectrange").toString().split(",")[0].toInt(), data[0].get("effectrange").toString().split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //span2.setSpan(StyleSpan(Typeface.BOLD), data[0].get("effectrange").toString().split(",")[0].toInt(), data[0].get("effectrange").toString().split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.blue)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

         */
       return binding.root
    }

    suspend fun submitList() {
        adapter1.submitList(temp1!!)
        adapter2.submitList(temp2)
        adapter3.submitList(temp3)
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            temp1 = async {  persnalRecommendInit() }.await()
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
                intent.putExtra("name", temp1!![position][0])
                (activity as MainActivity).addRecentRecipe(temp1!![position][0].toString(), temp1!![position][1].toString())
                startActivity(intent)
            }
        })
        adapter2.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp2[position][0])
                (activity as MainActivity).addRecentRecipe(temp2[position][0].toString(), temp2[position][1].toString())
                startActivity(intent)
            }
        })
        adapter3.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp3[position][0])
                (activity as MainActivity).addRecentRecipe(temp3[position][0].toString(), temp3[position][1].toString())
                startActivity(intent)
            }
        })

    }
    suspend fun persnalRecommendInit():MutableList<ArrayList<String>> {
        // recommend List
        var middle = mutableListOf<ArrayList<String>>()
        var database = FirebaseFirestore.getInstance()
        var result = arrayListOf<ArrayList<Int>>()
        var responseR = database.collection("Recipes").get()
            .await().toMutableList()
        var responseRMap = responseR.map { it.data["ingredient"] }

        var responseM = database.collection("ListData")
            .document(FirebaseAuth.getInstance().uid!!)
            .collection("Refrigerator")
            .get().await().toMutableList().map{it.data["ingredientname"]}

        responseRMap.forEachIndexed { index, doc ->
            var temp = (doc as List<String>) + responseM
            result.add(arrayListOf(index, temp.groupBy { it }.filter { it.value.size > 1 }.flatMap { it.value }.distinct().size))
        }
        var sortedResult = result.sortedBy { it[1] }.reversed()
        middle = mutableListOf<ArrayList<String>>(
            arrayListOf(responseR[sortedResult[0][0]].get("name").toString(), responseR[sortedResult[0][0]].get("icon").toString(),responseR[sortedResult[0][0]].get("title").toString(), responseR[sortedResult[0][0]].get("description").toString()),
            arrayListOf(responseR[sortedResult[1][0]].get("name").toString(), responseR[sortedResult[1][0]].get("icon").toString(),responseR[sortedResult[1][0]].get("title").toString(), responseR[sortedResult[1][0]].get("description").toString()),
            arrayListOf(responseR[sortedResult[2][0]].get("name").toString(), responseR[sortedResult[2][0]].get("icon").toString(),responseR[sortedResult[2][0]].get("title").toString(), responseR[sortedResult[2][0]].get("description").toString())
        )
        return middle
    }
    suspend fun recommendInit() {
        val response = FirebaseFirestore.getInstance().collection("Event").get()
            .await().toMutableList().sortedBy { it.get("eventidx").toString() }

        var temp2Response = response[0]
        var temp3Response = response[1]

        var temp2RecipeList = temp2Response.get("recipelist") as ArrayList<HashMap<String,Any>>
        temp2 = mutableListOf(
            arrayListOf(temp2RecipeList[0].get("name").toString(), temp2RecipeList[0].get("icon").toString(),temp2RecipeList[0].get("title").toString(), temp2RecipeList[0].get("description").toString()),
            arrayListOf(temp2RecipeList[1].get("name").toString(), temp2RecipeList[1].get("icon").toString(),temp2RecipeList[1].get("title").toString(), temp2RecipeList[1].get("description").toString()),
            arrayListOf(temp2RecipeList[2].get("name").toString(), temp2RecipeList[2].get("icon").toString(),temp2RecipeList[2].get("title").toString(), temp2RecipeList[2].get("description").toString())
        )
        var temp3RecipeList = temp3Response.get("recipelist") as ArrayList<HashMap<String,Any>>
        temp3 = mutableListOf(
            arrayListOf(temp3RecipeList[0].get("name").toString(), temp3RecipeList[0].get("icon").toString(),temp3RecipeList[0].get("title").toString(), temp3RecipeList[0].get("description").toString()),
            arrayListOf(temp3RecipeList[1].get("name").toString(), temp3RecipeList[1].get("icon").toString(),temp3RecipeList[1].get("title").toString(), temp3RecipeList[1].get("description").toString()),
            arrayListOf(temp3RecipeList[2].get("name").toString(), temp3RecipeList[2].get("icon").toString(),temp3RecipeList[2].get("title").toString(), temp3RecipeList[2].get("description").toString())
        )

        binding.mainRecommendTitle2.text = temp2Response.get("title").toString()
        binding.mainRecommendTitle3.text = temp3Response.get("title").toString()


        var span2: Spannable = binding.mainRecommendTitle2.text as Spannable
        var span3: Spannable = binding.mainRecommendTitle3.text as Spannable


        span2.setSpan(ForegroundColorSpan(Color.parseColor(temp2Response.get("effectcolor").toString())), temp2Response.get("effectrange").toString().split(",")[0].toInt(), temp2Response.get("effectrange").toString().split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span2.setSpan(StyleSpan(Typeface.BOLD), temp2Response.get("effectrange").toString().split(",")[0].toInt(), temp2Response.get("effectrange").toString().split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(ForegroundColorSpan(Color.parseColor(temp3Response.get("effectcolor").toString())), temp3Response.get("effectrange").toString().split(",")[0].toInt(), temp3Response.get("effectrange").toString().split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(StyleSpan(Typeface.BOLD), temp3Response.get("effectrange").toString().split(",")[0].toInt(), temp3Response.get("effectrange").toString().split(",")[1].toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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