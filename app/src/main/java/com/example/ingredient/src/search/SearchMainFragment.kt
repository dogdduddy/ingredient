package com.example.ingredient.src.search

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.R
import com.example.ingredient.common.RecipeDialogActivity
import com.example.ingredient.databinding.FragmentMainBinding

class SearchMainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get()  = _binding!!
    private var adapter1 = SearchMainAdapter()
    private var adapter2 = SearchMainAdapter()
    private var adapter3 = SearchMainAdapter()

    private var temp1 = mutableListOf<ArrayList<String>>(
        arrayListOf("비빔국수", "", "구수고소 비빔국수", "간장국수로 시~원하게"), arrayListOf("비빔국수","", "새콤달콤 에이드", "간장국수로 시~원하게"), arrayListOf("비빔국수","", "구수고소 미숫가루", "간장국수로 시~원하게"))
    private var temp2 = mutableListOf<ArrayList<String>>(
        arrayListOf("간장국수", "", "달콤구수 비빔국수", "간장국수로 시~원하게"), arrayListOf("비빔국수","", "새콤달콤 에이드", "간장국수로 시~원하게"), arrayListOf("비빔국수","", "달콤구수 미숫가루", "간장국수로 시~원하게"))
    private var temp3 = mutableListOf<ArrayList<String>>(
        arrayListOf("그런국수", "", "달콤고소 비빔국수", "간장국수로 시~원하게"), arrayListOf("비빔국수","", "새콤달콤 에이드", "간장국수로 시~원하게"), arrayListOf("비빔국수","", "달콤고소 미숫가루", "간장국수로 시~원하게"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        submitList()

        binding.mainRecommendTitle1.text = "집이 곧 카페가 되는 홈베이킹 레시피"
        binding.mainRecommendTitle2.text = "스트레스 다 날려버릴 매운맛 레시피"
        binding.mainRecommendTitle3.text = "추운 겨울을 따뜻하게 보낼 레시피"
        var span1: Spannable = binding.mainRecommendTitle1.text as Spannable
        var span2: Spannable = binding.mainRecommendTitle2.text as Spannable
        var span3: Spannable = binding.mainRecommendTitle3.text as Spannable
        span1.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.apricot)), 12, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span1.setSpan(StyleSpan(Typeface.BOLD), 12, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span2.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.red)), 12, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span2.setSpan(StyleSpan(Typeface.BOLD), 12, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(ForegroundColorSpan(requireActivity().getColor(R.color.blue)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
       return binding.root
    }

    fun submitList() {
        adapter1.submitList(temp1)
        adapter2.submitList(temp2)
        adapter3.submitList(temp3)
    }

    override fun onStart() {
        super.onStart()
        binding.mainSearchbar.setOnClickListener {
            val searchFragment = SearchFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.add(R.id.fragment_container, searchFragment)
            transaction?.commit()
        }
        adapter1.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp1[position][0])
                startActivity(intent)
            }
        })
        adapter2.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp2[position][0])
                startActivity(intent)
            }
        })
        adapter3.setItemClickListener(object: SearchMainAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", temp3[position][0])
                startActivity(intent)
            }
        })

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