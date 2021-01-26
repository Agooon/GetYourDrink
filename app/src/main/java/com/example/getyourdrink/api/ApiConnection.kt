package com.example.getyourdrink.api

import android.content.Context
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.runBlocking


fun getIngredientList(context: Context): List<Ingredient> {

    val listOfIngredients =
        Klaxon().parseArray<Ingredient>(context.assets.open("IngredientCall.json"))
    return listOfIngredients!!
}


suspend fun getDrinkList(context: Context): List<Drink> {

    val listOfDrinks = Klaxon().parseArray<Drink>(context.assets.open("DrinkCall.json"))
    return listOfDrinks!!
}

suspend fun getDrinkListIngr(context: Context, ingredients: List<String>): List<Drink> {

    val listOfDrinks = Klaxon().parseArray<Drink>(context.assets.open("DrinkCall.json"))

    var toReturn = mutableListOf<Drink>()
    var add = true
    if (listOfDrinks != null) {
        for (element in listOfDrinks) {
            add = true
            for (i in ingredients.indices) {
                if (!element.ingredients.map { it.name }.contains(ingredients[i])) {
                    add = false
                }
            }
            if (add) {
                toReturn.add(element)
            }
        }
    }
    return toReturn.toList()
}

suspend fun getDrink(context: Context, id: Long): Drink? {

    val listOfDrinks = Klaxon().parseArray<Drink>(context.assets.open("DrinkCall.json"))
    if (listOfDrinks != null) {
        listOfDrinks.forEach {
            if (it.id == id)
                return it
        }
    }
    return null
}


fun mainFun(context: Context) = runBlocking {

    val listIngredient = runBlocking { getIngredientList(context) }
    val listDrink = runBlocking { getDrinkList(context) }

    listIngredient.get(0).description

}





