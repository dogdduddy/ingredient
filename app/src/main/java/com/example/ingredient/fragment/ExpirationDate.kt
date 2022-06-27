package com.example.ingredient.fragment

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import com.example.ingredient.R
import com.example.ingredient.database.ExpirationDateDao
import com.example.ingredient.database.ExpirationDateDatabase
import com.example.ingredient.databinding.FragmentExpirationDateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpirationDate : Fragment() {
    private lateinit var db:ExpirationDateDatabase
    private var _binding:FragmentExpirationDateBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpirationDateBinding.inflate(layoutInflater, container, false)
        binding.btnTest.setOnClickListener {
            var text = binding.editTest.text.toString()
            CoroutineScope(Dispatchers.IO)
                .launch {
                    var newExpiration = com.example.ingredient.database.ExpirationDateData(text, "slice", "2022-04-09","2022-04-09")
                    db?.expirationDateDao()?.insert(newExpiration)
                }
            var exp = ""
            CoroutineScope(Dispatchers.IO)
                .launch {
                    exp = db?.expirationDateDao()?.getAll().toString()
                    Log.d("MainActivity", exp)
                }
        }
        return binding.root
    }


    companion object {
        fun newInstance(db: ExpirationDateDatabase) =
            ExpirationDate().apply {
                this.db = db
            }
    }
}
