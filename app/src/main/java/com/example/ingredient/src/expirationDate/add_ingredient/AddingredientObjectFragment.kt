package com.example.ingredient.src.expirationDate.add_ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ingredient.R
import org.w3c.dom.Text

class AddingredientObjectFragment : Fragment() {
    private val ARG_OBJECT = "object"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addingredient_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey((ARG_OBJECT)) }?.apply {
            val textView: TextView = view.findViewById(R.id.object_Text)
            textView.text = getInt(ARG_OBJECT).toString()
        }
    }
}