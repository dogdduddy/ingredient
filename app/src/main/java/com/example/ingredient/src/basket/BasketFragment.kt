package com.example.ingredient.src.basket

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.databinding.FragmentBasketBinding
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.android.material.tabs.TabLayoutMediator



class BasketFragment : Fragment(), BasketView {

    private var _binding : FragmentBasketBinding? = null
    private val binding get()  = _binding!!
    private lateinit var viewPager:ViewPager2
    private lateinit var basketViewPagerAdapter:BasketViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasketBinding.inflate(layoutInflater, container, false)
        viewPager = binding.basketViewpager
        basketViewPagerAdapter = BasketViewPagerAdapter(this)
        viewPager.adapter = basketViewPagerAdapter

        // 임시 카테고리 이름 데이터
        var tablayerName = arrayListOf("그룹", "총재료")

        // 카테고리 적용
        TabLayoutMediator(binding.basketTabLayout, viewPager) { tab, position ->
            tab.text = tablayerName[position]
        }.attach()

        BasketService(this@BasketFragment).getBasket()

        return binding.root
    }

    fun UpdateData(data : ArrayList<BasketIngredient>) {
        basketViewPagerAdapter.submitList(data)
    }
    companion object {
        fun newInstance() = BasketFragment()
    }

    override fun onGetBasketSuccess(response: ArrayList<BasketIngredient>) {
        UpdateData(response)
    }

    override fun onGetBasketFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetBasketGroupSuccess(response: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun onGetBasketGroupFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupListSuccess(response: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupIngredientSuccess(response: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupListFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupIngredientFailure(message: String) {
        TODO("Not yet implemented")
    }
}

