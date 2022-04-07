package com.example.ingredient.fragment

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.ingredient.R
import com.example.ingredient.database.ExpirationDateDao
import com.example.ingredient.database.ExpirationDateDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpirationDate : Fragment() {
    private lateinit var db:ExpirationDateDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = ExpirationDateDatabase.getInstance(Application())
        CoroutineScope(Dispatchers.IO)
            .launch {
                var newExpiration = com.example.ingredient.database.ExpirationDateData("egg soup", "slice", "2022-04-09","2022-04-09")
                db?.expirationDateDao()?.insert(newExpiration)
            }

        var exp = ""
        CoroutineScope(Dispatchers.IO)
            .launch {
                exp = db?.expirationDateDao()?.getAll().toString()
                Log.d("MainActivity", exp)
            }
        return inflater.inflate(R.layout.fragment_expiration_date, container, false)
    }

    companion object {
        fun newInstance(db: ExpirationDateDatabase) =
            ExpirationDate().apply {
                this.db = db
            }
    }
}