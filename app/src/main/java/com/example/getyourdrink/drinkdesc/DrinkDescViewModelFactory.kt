package com.example.getyourdrink.drinkdesc

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DrinkDescViewModelFactory(
    private val application: Application,
    private val id: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrinkDescViewModel::class.java)) {
            return DrinkDescViewModel(application, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}