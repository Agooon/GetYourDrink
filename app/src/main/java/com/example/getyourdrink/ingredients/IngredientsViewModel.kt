package com.example.getyourdrink.ingredients

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getyourdrink.api.Ingredient
import com.example.getyourdrink.api.getIngredientList


class IngredientsViewModel(
    context: Context
) : ViewModel() {
    private var _selectedIngredients = MutableLiveData<List<Ingredient>>()
    val selectedIngredients: LiveData<List<Ingredient>>
        get() = _selectedIngredients
    val ingredients: List<Ingredient> = getIngredientList(context)

    init {
        _selectedIngredients.value = listOf<Ingredient>()
    }


    // When selecting checkbox
    fun onAdd(ingredient: Ingredient) {
        if (!_selectedIngredients.value!!.contains(ingredient)) {
            val newSelIngrList = _selectedIngredients.value!!.plus(ingredient)
            _selectedIngredients.value = newSelIngrList
        }
    }

    // When deselecting checkbox
    fun onRemove(ingredient: Ingredient) {
        if (_selectedIngredients.value!!.contains(ingredient)) {
            val newSelIngrList = _selectedIngredients.value!!.minus(ingredient)
            _selectedIngredients.value = newSelIngrList
        }
    }

    fun getSelectedIngredientsStr() {
        val nameIngredientList: List<String> = _selectedIngredients.value!!.map { it -> it.name }
    }
}