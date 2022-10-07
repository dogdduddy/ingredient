package com.example.ingredient.src.basket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.databinding.FragmentBasketBinding
import com.example.ingredient.src.basket.group.GroupAddIngredientsActivity
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Basket : Fragment() {

    private var _binding : FragmentBasketBinding? = null
    private val binding get()  = _binding!!
    private lateinit var viewPager:ViewPager2
    private lateinit var database:FirebaseFirestore
    private lateinit var basketViewPagerAdapter:BasketViewPagerAdapter
    private var userid:String? = null
    private var basketData = ArrayList<BasketIngredient>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasketBinding.inflate(layoutInflater, container, false)
        viewPager = binding.basketViewpager
        basketViewPagerAdapter = BasketViewPagerAdapter(this)
        viewPager.adapter = basketViewPagerAdapter

        userid = FirebaseAuth.getInstance().uid!!
        database = FirebaseFirestore.getInstance()
        database.collection("ListData")
            .document(userid!!)
            .collection("Basket")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    basketData.add(BasketIngredient(
                        document.get("ingredienticon").toString(),
                        document.get("ingredientidx").toString().toInt(),
                        document.get("ingredientname").toString(),
                        document.get("ingredientcategory").toString(),
                        document.get("basketgroup").toString(),
                        document.get("ingredientquantity").toString().toInt()
                    ))
                }
                UpdateData(basketData)
            }
        binding.basketAddButton.setOnClickListener {
            addIngredientBtn()
        }
        return binding.root
    }

    fun UpdateData(data : ArrayList<BasketIngredient>) {
        basketViewPagerAdapter.submitList(data)
    }
    companion object {
        fun newInstance() =
            Basket()
    }

    fun addIngredientBtn() {
        var basketList = arrayOf<String>()

        database.collection("ListData")
            .document(userid!!)
            .collection("BasketList")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    basketList = basketList.plus(document.get("groupName").toString())
                }
                var intent = Intent(context, GroupAddIngredientsActivity::class.java)
                intent.putExtra("groupList", basketList)
                startActivity(intent)
            }
    }
}

