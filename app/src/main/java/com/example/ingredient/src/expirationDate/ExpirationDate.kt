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
import com.example.ingredient.data.ExpirationDateDatabase
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
            var intent = Intent(activity, AddingredientsActivity::class.java)
            startActivity(intent)
        }
        binding.radioSelectButton.setOnClickListener {
            // Intent로 Activity를 넘기면서, PutExtra에 해당 값을 저장
            // 칼럼 : 재료 상태, 보관 방식
            when(binding.radioGroup.checkedRadioButtonId) {
                binding.radioButton1.id -> binding.editTest.setText("Button1")
                binding.radioButton2.id -> binding.editTest.setText("Button2")
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
