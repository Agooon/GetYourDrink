package com.example.getyourdrink.start

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.getyourdrink.R
import com.example.getyourdrink.databinding.FragmentStartBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStartBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_start, container, false
        )
        // Inflate the layout for this fragment

        binding.logoImage.setOnClickListener {

            ObjectAnimator.ofFloat(binding.logoImage, "translationX", 1200f).apply {
                duration = 1000
                start()
            }
            ObjectAnimator.ofFloat(binding.titleTextTop, "translationX", -1200f).apply {
                duration = 1000
                start()
            }
            this.findNavController().navigate(StartFragmentDirections.actionStartFragmentToIngredientsFragment())
        }

        return binding.root
    }


}