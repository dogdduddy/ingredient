package com.example.ingredient.src.basket.group.add_ingredient

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ingredient.R
import com.example.ingredient.databinding.FragmentAddIngredientListBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddGroupIngredientListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddGroupIngredientListFragment (var ingredients: CategoryIngrediets) : Fragment() {
    private var addgroupingredientView:AddGroupIngredientsActivity? = null
    private var _binding: FragmentAddIngredientListBinding? = null
    private val binding get() = _binding!!
    private lateinit var ingredientRecyclerViewadApter:AddGroupIngredientListAdapter
    //var ingredients: CategoryIngrediets? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddIngredientListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ingredientRecyclerViewadApter = AddGroupIngredientListAdapter(addgroupingredientView!!)

        binding.rvIngredient.apply {
            adapter = ingredientRecyclerViewadApter
            layoutManager = GridLayoutManager(context, 4)
        }

        ingredientRecyclerViewadApter.submitList(ArrayList(ingredients?.ingredientList))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is AddGroupIngredientsActivity) {
            addgroupingredientView = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        addgroupingredientView = null
    }
}