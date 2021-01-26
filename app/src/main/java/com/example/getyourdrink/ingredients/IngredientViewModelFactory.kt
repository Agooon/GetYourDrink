package com.example.getyourdrink.ingredients

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class IngredientViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientsViewModel::class.java)) {
            return IngredientsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}