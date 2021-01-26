package com.example.getyourdrink.drinkdesc

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.getyourdrink.api.Drink
import com.example.getyourdrink.api.getDrink
import kotlinx.coroutines.runBlocking


class DrinkDescViewModel(
    val context: Context,
    val id: Long
) : ViewModel() {
    lateinit var drink: Drink

    init {
        drink = runBlocking { getDrink(context, id)!! }
    }

    fun getIngredientToString(): String {
        var strToReturn = ""
        for (ingredient in drink.ingredients) {
            strToReturn += ingredient.id.toString() + ". " + ingredient.name + " | Amount:" + ingredient.amount
        }
        return strToReturn
    }

}