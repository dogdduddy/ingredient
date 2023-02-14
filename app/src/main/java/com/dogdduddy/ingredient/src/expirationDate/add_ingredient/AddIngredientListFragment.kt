package com.dogdduddy.ingredient.src.expirationDate.add_ingredient

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dogdduddy.ingredient.R
import com.dogdduddy.ingredient.databinding.FragmentAddIngredientListBinding
import com.dogdduddy.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.dogdduddy.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AddIngredientListFragment() : Fragment() {
    private var addingredientActivityView:AddIngredientsActivity? = null
    private var _binding: FragmentAddIngredientListBinding? = null
    private val binding get() = _binding!!
    private var ingredients:CategoryIngrediets? = null
    private lateinit var ingredientRecyclerViewadApter:AddIngredientListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ingredients = it.getParcelable("ingredients")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddIngredientListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ingredientRecyclerViewadApter = AddIngredientListAdapter(addingredientActivityView!!)

        binding.rvIngredient.apply {
            adapter = ingredientRecyclerViewadApter
            layoutManager = GridLayoutManager(context, 4)
        }

        ingredientRecyclerViewadApter.submitList(ArrayList(ingredients?.ingredientlist))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is AddIngredientsActivity) {
            addingredientActivityView = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        addingredientActivityView = null
    }
}