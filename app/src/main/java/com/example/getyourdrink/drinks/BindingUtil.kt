package com.example.getyourdrink.drinks

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.getyourdrink.api.Drink
import com.squareup.picasso.Picasso


@BindingAdapter("drinkName")
fun TextView.setIngredientName(item: Drink) {
    if (item.id == 1L) {
        this.setTextColor(Color.CYAN)
    }
    text = item.name
}

@BindingAdapter("drinkImage")
fun ImageView.setIngredientImage(item: Drink) {
    Picasso.get().load(item.imagePath).fit().into(this)
}