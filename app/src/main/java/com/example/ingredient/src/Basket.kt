package com.example.ingredient.src

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.example.ingredient.databinding.FragmentNoteBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient

class Basket : Fragment() {

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
            com.example.ingredient.src.Basket()
    }
}