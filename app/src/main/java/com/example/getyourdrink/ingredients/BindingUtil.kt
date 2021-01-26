package com.example.getyourdrink.ingredients

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.getyourdrink.R
import com.example.getyourdrink.api.Ingredient


@BindingAdapter("ingredientName")
fun TextView.setIngredientName(item: Ingredient) {
    text = item.name
}

@BindingAdapter("ingredientImage")
fun ImageView.setIngredientImage(item: Ingredient) {
    setImageResource(R.drawable.ingredient_icon)
}