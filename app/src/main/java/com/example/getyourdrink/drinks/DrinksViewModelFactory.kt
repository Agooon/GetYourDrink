package com.example.getyourdrink.drinks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DrinksViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinksViewModel::class.java)) {
            return DrinksViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}