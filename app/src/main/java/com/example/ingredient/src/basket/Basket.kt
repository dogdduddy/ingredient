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

        // Init Adapter Fragment
        var testFragments = arrayListOf(TestFragment1(), TestFragment2())
        basketViewPagerAdapter.setFragment(testFragments)

        viewPager.adapter = basketViewPagerAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
    }
    companion object {
        fun newInstance() =
            Basket()
    }
}