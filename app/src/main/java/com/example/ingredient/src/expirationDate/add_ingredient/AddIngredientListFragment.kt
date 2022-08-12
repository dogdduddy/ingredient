package com.example.ingredient.src.expirationDate.add_ingredient

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.R
import com.example.ingredient.databinding.FragmentAddIngredientListBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AddIngredientListFragment : Fragment() {
    private var addingredientActivityView:AddIngredientsActivity? = null
    private var _binding: FragmentAddIngredientListBinding? = null
    private val binding get() = _binding!!
    private lateinit var ingredientRecyclerViewadApter:AddIngredientListAdapter
    var ingredients: CategoryIngrediets? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddIngredientListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.takeIf { it.containsKey("ingredients") }?.apply {
            ingredients = getParcelable("ingredients")!!
            Log.d("AddFragment", ingredients.toString())
        }
        ingredientRecyclerViewadApter = AddIngredientListAdapter(addingredientActivityView!!)
        binding.rvIngredient.adapter = ingredientRecyclerViewadApter

        binding.rvIngredient.adapter = ingredientRecyclerViewadApter
        binding.rvIngredient.layoutManager = GridLayoutManager(context, 4)

        ingredientRecyclerViewadApter.submitList(ArrayList(ingredients?.ingredientList))
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
    fun changedClickable(ingredientName:String, position:Int) {
        ingredientRecyclerViewadApter.changedData(ingredientName, position)
    }
}