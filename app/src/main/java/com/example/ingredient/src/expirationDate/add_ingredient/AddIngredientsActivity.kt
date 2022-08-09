package com.example.ingredient.src.expirationDate.add_ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityAddingredientsBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AddIngredientsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddingredientsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var ingredientViewPagerAdapter:AddIngredientViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddingredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewpagerAddIngredient
        ingredientViewPagerAdapter = AddIngredientViewPagerAdapter(this, this)
        viewPager.adapter = ingredientViewPagerAdapter

        val carrot = Ingredient("carrot", 0,"Carrot")
        val potato = Ingredient("potato", 0,"potato")
        val chicken = Ingredient("chicken", 0,"chicken")
        val fish = Ingredient("fish", 3,"fish")

        val all =  CategoryIngrediets(0, "전체", listOf(carrot, potato, chicken, fish))
        val bag = CategoryIngrediets(1, "채소", listOf(carrot, potato))
        val meat = CategoryIngrediets(2, "육류", listOf(chicken))
        val seafood = CategoryIngrediets(3, "해산물", listOf(fish))

        val category = listOf(all, bag, meat, seafood)


        var ingredients = ArrayList<CategoryIngrediets>()
        var tablayerName = ArrayList<String>()

        ingredients.clear()
        category.forEach {
            ingredients.add(it)
            tablayerName.add(it.ingredientCategoryName)
        }

        TabLayoutMediator(binding.tabLayout, viewPager, {tab, position ->
            tab.text = tablayerName[position]
        }).attach()

        ingredientViewPagerAdapter.submitList(ingredients)
    }
}