package com.example.ingredient.src.foodbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ingredient.R
import com.example.ingredient.databinding.FragmentMainBinding
import com.example.ingredient.src.search.SearchFragment

class FoodbookMain : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get()  = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.mainSearchbar.setOnClickListener {
            val foodBookFragment = FoodBookFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.add(R.id.fragment_container, foodBookFragment)
            transaction?.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FoodbookMain()
    }

}