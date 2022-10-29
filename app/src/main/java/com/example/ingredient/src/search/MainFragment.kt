package com.example.ingredient.src.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.ingredient.R
import com.example.ingredient.databinding.FragmentMainBinding
import com.example.ingredient.databinding.FragmentSearchBinding

class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get()  = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.mainSearchbar.setOnClickListener {
            val searchFragment = Search()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.add(R.id.fragment_container, searchFragment)
            transaction?.commit()
        }
    }
    companion object {
        fun newInstance() =
            MainFragment()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}