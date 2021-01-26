package com.example.getyourdrink.drinks

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.getyourdrink.R
import com.example.getyourdrink.api.Drink


@BindingAdapter("drinkName")
fun TextView.setIngredientName(item: Drink) {
    text = item.name
}

@BindingAdapter("drinkImage")
fun ImageView.setIngredientImage(item: Drink) {
    setImageResource(R.drawable.drink_glass_icon)
}