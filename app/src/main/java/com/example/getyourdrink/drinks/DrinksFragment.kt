package com.example.getyourdrink.drinks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.getyourdrink.R
import com.example.getyourdrink.databinding.FragmentDrinksBinding


class DrinksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDrinksBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_drinks, container, false
        )
        // Inflate the layout for this fragment


        val application = requireNotNull(this.activity).application

        val viewModelFactory = DrinksViewModelFactory(application)

        // Declaration of view model
        val drinkViewModel =
            ViewModelProvider(this, viewModelFactory).get(DrinksViewModel::class.java)

        val passedIngredients = requireArguments()["ingredientsName"] as Array<String>
        if (passedIngredients.isEmpty() || passedIngredients[0] == "all")
            drinkViewModel.getAllDrinks()
        else
            drinkViewModel.getDrinksFromIngredients(passedIngredients)


        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 3
            }
        }

        val adapter = DrinksAdapter(DrinkListener { drink ->
            // Navigating to single drink
            this.findNavController().navigate(
                DrinksFragmentDirections.actionDrinksFragmentToDrinkDescFragment(drink.id.toInt())
            )
        })

        binding.shakeAgain.setOnClickListener {
            this.findNavController().navigate(
                DrinksFragmentDirections.actionDrinksFragmentToShakerFragment(passedIngredients)
            )
        }
        binding.mainMenu.setOnClickListener {
            this.findNavController().navigate(
                DrinksFragmentDirections.actionDrinksFragmentToStartFragment()
            )
        }

        binding.drinkViewModel = drinkViewModel

        binding.drinksList.adapter = adapter

        adapter.addHeaderAndSubmitList(drinkViewModel.drinks)

        binding.drinksList.layoutManager = manager

        if (drinkViewModel.drinks.isEmpty()) {
            binding.textInfo.visibility = View.VISIBLE
        } else {
            binding.textInfo.visibility = View.INVISIBLE
        }

        return binding.root
    }
}