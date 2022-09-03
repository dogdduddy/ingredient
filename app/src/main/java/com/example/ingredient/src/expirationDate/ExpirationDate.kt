package com.example.ingredient.src.expirationDate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.database.ExpirationDateDao
import com.example.ingredient.database.ExpirationDateDatabase
import com.example.ingredient.databinding.FragmentExpirationDateBinding
import com.example.ingredient.src.expirationDate.add_ingredient.AddIngredientsActivity
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient

class ExpirationDate : Fragment() {
    private lateinit var db:ExpirationDateDatabase
    private var _binding:FragmentExpirationDateBinding? = null
    private val binding get() = _binding!!
    private var expiryDates = ArrayList<ExpiryDateIngredient>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpirationDateBinding.inflate(layoutInflater, container, false)

        /* Room Data
        binding.btnTest.setOnClickListener {
            var text = binding.editTest.text.toString()
            CoroutineScope(Dispatchers.IO)
                .launch {
                    var newExpiration = com.example.ingredient.data.ExpirationDateData(text, "slice", "2022-04-09","2022-04-09")
                    db?.expirationDateDao()?.insert(newExpiration)
                }
            var exp = ""
            CoroutineScope(Dispatchers.IO)
                .launch {
                    exp = db?.expirationDateDao()?.getAll().toString()
                    Log.d("MainActivity", exp)
                }
        }

         */
        binding.btnTest.setOnClickListener {
            var intent = Intent(activity, AddIngredientsActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }
    fun ExpirationDateSubmit(expiryDate:ArrayList<ExpiryDateIngredient>) {
        expiryDates.addAll(expiryDate)
        Log.d("stateTest", "유통기한 : ${expiryDates}")
        Log.d("stateTest", "유통기한 : ${expiryDates[0]}")
    }
    companion object {
        fun newInstance(db: ExpirationDateDatabase) =
            ExpirationDate().apply {
                this.db = db
            }
    }
}
