package com.example.getyourdrink.api

import com.beust.klaxon.Json


class Drink(
    @Json(name = "Id") val id: Long,
    @Json(name = "Name") val name: String,
    @Json(name = "Ingredients") val ingredients: List<IngredientSimple>,
    @Json(name = "Description") val description: String,
    @Json(name = "ImagePath") val imagePath: String
)
