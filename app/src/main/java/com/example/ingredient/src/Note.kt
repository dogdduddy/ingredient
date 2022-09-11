package com.example.ingredient.src

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.database.ExpirationDateDatabase
import com.example.ingredient.databinding.FragmentNoteBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Note : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get()  = _binding!!
    private lateinit var test:Array<ExpiryDateIngredient?>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        test = arrayOfNulls<ExpiryDateIngredient>(5)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        binding.checkBtn.setOnClickListener {
            Log.d("radiogTest", "test : ${binding.radiog.checkedRadioButtonId}")
        }
    }
    companion object {
        fun newInstance() =
            com.example.ingredient.src.Note()
    }
}