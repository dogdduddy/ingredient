package com.example.ingredient.src.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketIngredient

class TotalIngredientsFragment(basketList: ArrayList<BasketIngredient>): Fragment() {
    private var basketList = basketList

    // 총재료 (아이콘, 재료ID, 재료명, 카테고리, 수량)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_totalingredients, container, false)
        var t = view?.findViewById<TextView>(R.id.totalText)
        t!!.text = basketList[0].categoryName
        return view
    }
}