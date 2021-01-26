package com.example.getyourdrink.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.getyourdrink.R
import com.example.getyourdrink.databinding.FragmentIngredientsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IngredientsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IngredientsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentIngredientsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_ingredients, container, false
        )
        // Inflate the layout for this fragment

        val application = requireNotNull(this.activity).application

        val viewModelFactory = IngredientViewModelFactory(application)

        // Declaration of view model
        val ingredientViewModel =
            ViewModelProvider(this, viewModelFactory).get(IngredientsViewModel::class.java)


        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }

        val adapter = IngredientAdapter(IngredientListener { ingredient ->

            if (!ingredientViewModel.selectedIngredients.value!!.contains(ingredient)) {
                ingredientViewModel.onAdd(ingredient)
                changeBackground(manager, true, ingredient.id)
            } else {
                ingredientViewModel.onRemove(ingredient)
                changeBackground(manager, false, ingredient.id)
            }

        })

        binding.button.setOnClickListener {
            if (ingredientViewModel.selectedIngredients.value!!.isEmpty()) {
                Toast.makeText(context, "You have to add some ingredients!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val ingredientNames =
                    ingredientViewModel.selectedIngredients.value!!.map { it.name }.toTypedArray()
                this.findNavController().navigate(
                    IngredientsFragmentDirections.actionIngredientsFragmentToShakerFragment(
                        ingredientNames
                    )
                )
            }
        }

        binding.ingredientsViewModel = ingredientViewModel

        binding.ingredientsList.adapter = adapter

        adapter.addHeaderAndSubmitList(ingredientViewModel.ingredients)

        binding.ingredientsList.layoutManager = manager

        return binding.root
    }


    fun changeBackground(manager: GridLayoutManager, add: Boolean, id: Long) {
        try {
            if (add) {
                (manager.findViewByPosition(id.toInt()) as ViewGroup).children.forEach {
                    it.background = (ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.layout_bg_violet
                    ))
                }
            } else {
                (manager.findViewByPosition(id.toInt()) as ViewGroup).children.forEach {
                    it.background = (ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.layout_bg_navyblue
                    ))
                }
            }
        } catch (Exception: Exception) {
            print(Exception.message)
        }

    }
}