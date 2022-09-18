package com.example.ingredient.src.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ingredient.R

class TestFragment2 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test2, container, false)
    }

    fun setText(text:String) {
        var test: TextView = requireView().findViewById(R.id.fragment2_text)
        test.text = text
    }
    companion object {
        @JvmStatic
        fun newInstance() = TestFragment2()
    }
}