package com.example.getyourdrink.drinkdesc

import android.content.Context
import android.text.Spanned
import androidx.lifecycle.ViewModel
import com.example.getyourdrink.api.Drink
import com.example.getyourdrink.api.getDrink
import com.example.getyourdrink.formatDescription
import com.example.getyourdrink.formatIngredients
import kotlinx.coroutines.runBlocking


class DrinkDescViewModel(
    val context: Context,
    val id: Long
) : ViewModel() {
    lateinit var drink: Drink
    lateinit var descriptionSpan: Spanned
    lateinit var urlImage: String
    lateinit var ingredientString: Spanned

    init {
        drink = runBlocking { getDrink(context, id)!! }
        descriptionSpan = formatDescription(drink.description)
        urlImage = drink.imagePath
        ingredientString = formatIngredients(drink.ingredients)
    }

}