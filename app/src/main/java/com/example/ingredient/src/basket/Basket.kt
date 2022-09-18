package com.example.ingredient.src.basket

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.databinding.FragmentBasketBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
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

        database = FirebaseFirestore.getInstance()
        database.collection("")

        // Init Adapter Fragment
        var basketFragment = arrayListOf(GroupIngredientsFragment(), TotalIngredientsFragment())
        basketViewPagerAdapter.setFragment(basketFragment)

        viewPager.adapter = basketViewPagerAdapter
        return binding.root
    }
    companion object {
        fun newInstance() =
            Basket()
    }
}