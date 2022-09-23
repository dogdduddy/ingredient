package com.example.ingredient.src.basket

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.databinding.FragmentBasketBinding
import com.example.ingredient.src.basket.models.BasketIngredient
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Basket : Fragment() {

    private var _binding : FragmentBasketBinding? = null
    private val binding get()  = _binding!!
    private lateinit var viewPager:ViewPager2
    private lateinit var database:FirebaseFirestore
    private lateinit var basketViewPagerAdapter:basketViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasketBinding.inflate(layoutInflater, container, false)
        viewPager = binding.basketViewpager
        basketViewPagerAdapter = basketViewPagerAdapter(this)
        viewPager.adapter = basketViewPagerAdapter


        // // 재료 (아이콘, 재료id, 재료명, 카테고리, 그룹명, 수량)
        var userid = FirebaseAuth.getInstance().uid!!
        database = FirebaseFirestore.getInstance()
        database.collection("ListData")
            .document(userid)
            .collection("Basket")
            .get()
            .addOnSuccessListener { documents ->
                var temp = ArrayList<BasketIngredient>()
                for(document in documents) {
                    temp.add(BasketIngredient(
                        document.get("ingredienticon").toString(),
                        document.get("ingredientidx").toString().toInt(),
                        document.get("ingredientname").toString(),
                        document.get("ingredientcategory").toString(),
                        document.get("basketgroup").toString(),
                        document.get("ingredientquantity").toString().toInt()
                    ))
                }
                UpdateData(temp)
            }
        binding.basketBtn.setOnClickListener {
            testbtn()
        }
        return binding.root
    }
    fun testbtn() {
        var userid = FirebaseAuth.getInstance().uid!!
        /*
        viewPager = binding.basketViewpager
        basketViewPagerAdapter = basketViewPagerAdapter(this)
        viewPager.adapter = basketViewPagerAdapter

         */
        database.collection("ListData")
            .document(userid)
            .collection("Basket")
            .get()
            .addOnSuccessListener { documents ->
                var temp = ArrayList<BasketIngredient>()
                for(document in documents) {
                    temp.add(BasketIngredient(
                        document.get("ingredienticon").toString(),
                        document.get("ingredientidx").toString().toInt(),
                        document.get("ingredientname").toString(),
                        "육류",
                        "돼지김치찌개",
                        document.get("ingredientquantity").toString().toInt()
                    ))
                }
                UpdateData(temp)
            }
    }
    fun UpdateData(data : ArrayList<BasketIngredient>) {
        Log.d("basketTest","test 1 : ${data[0].categoryName}")
        basketViewPagerAdapter.submitList(data)
    }
    companion object {
        fun newInstance() =
            Basket()
    }
}

