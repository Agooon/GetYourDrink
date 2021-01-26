package com.example.getyourdrink.start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

    lateinit var binding: FragmentStartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_start, container, false
        )
        // Inflate the layout for this fragment


        binding.drinkLeft.setOnClickListener {
            animateToDrinks()
        }

        binding.ingredientRight.setOnClickListener {
            animateToIngredients()
        }

        binding.logoImage.setOnClickListener {
            animateToIngredients()
        }

        return binding.root
    }

    fun animateToDrinks() {
        val animSet = AnimatorSet()
        val animSet2 = AnimatorSet()

        val animTime: Long = 550
        animSet.playTogether(
            ObjectAnimator.ofFloat(binding.logoImage, "translationX", 1200f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.titleTextTop, "translationX", -1200f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.titleTextBottom, "translationX", 1200f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.drinkLeft, "translationX", 250f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.drinkLeft, "translationY", 300f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.ingredientRight, "translationX", -1200f)
                .setDuration(animTime),
        )
        animSet.start()

        requireView().postDelayed({
            val animTime: Long = 550
            animSet2.playTogether(
                ObjectAnimator.ofFloat(binding.drinkLeft, "scaleX", 5f).setDuration(animTime),
                ObjectAnimator.ofFloat(binding.drinkLeft, "scaleY", 5f).setDuration(animTime),
            )
            animSet2.start()
        }, 550)

        requireView().postDelayed({
            this.findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToDrinksFragment(
                    arrayOf<String>("all")
                )
            )
        }, 1050)
    }


    fun animateToIngredients() {
        val animSet = AnimatorSet()
        val animSet2 = AnimatorSet()

        val animTime: Long = 550
        animSet.playTogether(
            ObjectAnimator.ofFloat(binding.logoImage, "translationX", 1200f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.titleTextTop, "translationX", -1200f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.titleTextBottom, "translationX", 1200f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.drinkLeft, "translationX", 1200f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.ingredientRight, "translationX", -250f)
                .setDuration(animTime),
            ObjectAnimator.ofFloat(binding.ingredientRight, "translationY", 300f)
                .setDuration(animTime),
        )
        animSet.start()


        requireView().postDelayed({
            val animTime: Long = 550
            animSet2.playTogether(
                ObjectAnimator.ofFloat(binding.ingredientRight, "scaleX", 5f).setDuration(animTime),
                ObjectAnimator.ofFloat(binding.ingredientRight, "scaleY", 5f).setDuration(animTime),
            )
            animSet2.start()
        }, 550)

        requireView().postDelayed({
            this.findNavController()
                .navigate(StartFragmentDirections.actionStartFragmentToIngredientsFragment())
        }, 1050)
    }

}