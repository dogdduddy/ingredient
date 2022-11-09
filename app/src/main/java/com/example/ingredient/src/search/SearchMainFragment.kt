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
        arrayListOf("스콘", "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%A9%E1%86%AB.jpeg?alt=media&token=383d4f57-f75c-466e-811b-f92ea2af101e", "고소담백 스콘", "커피와 함께 고소하게"), arrayListOf("마들렌","https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%86%E1%85%A1%E1%84%83%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A6%E1%86%AB.jpeg?alt=media&token=423a7633-7378-4eee-b607-79a3e58f9e36", "부드러운 마들렌", "레몬향과 부드러운 식감"), arrayListOf("땅콩버터쿠키","https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%84%E1%85%A1%E1%86%BC%E1%84%8F%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A5%E1%84%90%E1%85%A5%E1%84%8F%E1%85%AE%E1%84%8F%E1%85%B5.jpeg?alt=media&token=c5c91c4e-6214-4566-8321-dbe5d438dbca", "땅콩버터 쿠키", "향긋한 버터향의 쿠키"))
    private var temp2 = mutableListOf<ArrayList<String>>(
        arrayListOf("매운어묵꼬치", "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%86%E1%85%A2%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%8B%E1%85%A5%E1%84%86%E1%85%AE%E1%86%A8%E1%84%81%E1%85%A9%E1%84%8E%E1%85%B5.jpeg?alt=media&token=8165f0bc-8c5b-468d-99d7-5c33218946f0", "매콤얼큰 어묵꼬치", "뜨끈한 국물을 시원하게"), arrayListOf("쭈꾸미볶음","https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%8D%E1%85%AE%E1%84%81%E1%85%AE%E1%84%86%E1%85%B5%E1%84%87%E1%85%A9%E1%86%A9%E1%84%8B%E1%85%B3%E1%86%B7.jpeg?alt=media&token=9a68521b-6884-40fc-9736-2a800ed68567", "달콤화끈 쭈꾸미볶음", "매콤한 바다향을 느끼며"), arrayListOf("불닭팽이","https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%87%E1%85%AE%E1%86%AF%E1%84%83%E1%85%A1%E1%86%B0%E1%84%91%E1%85%A2%E1%86%BC%E1%84%8B%E1%85%B5.jpeg?alt=media&token=913b4e55-fdcb-4c85-9726-0c083a2340a1", "인생버섯 불닭팽이", "아는 맛을 식감좋게"))
    private var temp3 = mutableListOf<ArrayList<String>>(
        arrayListOf("두부전골", "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%83%E1%85%AE%E1%84%87%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%AB%E1%84%80%E1%85%A9%E1%86%AF.jpeg?alt=media&token=a16a5892-d1da-44bf-95b1-1b2aec7946e2", "담백깔끔 두부전골", "깔끔함으로 승부하는"), arrayListOf("호떡","https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%92%E1%85%A9%E1%84%84%E1%85%A5%E1%86%A8.jpeg?alt=media&token=8d488e96-ae59-4ef7-82d7-83797d769ea6", "달콤고소 겨울호떡", "달콤함을 쫀득하게"), arrayListOf("칼국수","https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%8F%E1%85%A1%E1%86%AF%E1%84%80%E1%85%AE%E1%86%A8%E1%84%89%E1%85%AE.jpeg?alt=media&token=6b569ce6-52df-4835-8b48-df94c7eafd98", " 속이뜨끈 칼국수", "뜨끈한 국물로 시원하게"))


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