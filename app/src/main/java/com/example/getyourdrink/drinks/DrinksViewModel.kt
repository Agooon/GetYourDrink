package com.example.getyourdrink.drinks

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.getyourdrink.api.Drink
import com.example.getyourdrink.api.getDrinkList
import com.example.getyourdrink.api.getDrinkListIngr
import kotlinx.coroutines.runBlocking


class DrinksViewModel(
    val context: Context
) : ViewModel() {
    lateinit var drinks: List<Drink>

    fun getAllDrinks() {
        drinks = runBlocking { getDrinkList(context) }
    }

    fun getDrinksFromIngredients(ingredientsNames: Array<String>) {
        drinks = runBlocking { getDrinkListIngr(context, ingredientsNames.toList()) }
    }


}