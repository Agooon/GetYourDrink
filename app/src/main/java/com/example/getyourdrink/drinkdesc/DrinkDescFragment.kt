package com.example.getyourdrink.drinkdesc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.getyourdrink.R
import com.example.getyourdrink.databinding.FragmentDrinkDescBinding
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DrinkDescFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrinkDescFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentDrinkDescBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_drink_desc, container, false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory =
            DrinkDescViewModelFactory(application, (requireArguments()["id"] as Int).toLong())

        // Declaration of view model
        val drinkDescViewModel =
            ViewModelProvider(this, viewModelFactory).get(DrinkDescViewModel::class.java)

        binding.drinkDescViewModel = drinkDescViewModel
        //drinkIcon
        Picasso.get().load(drinkDescViewModel.urlImage).fit().into(binding.drinkIcon)

        return binding.root
    }

}