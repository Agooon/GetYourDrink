package com.example.getyourdrink.api

import com.beust.klaxon.Json

class IngredientSimple(
    @Json(name = "Id") val id: Long,
    @Json(name = "Name") val name: String,
    @Json(name = "Amount") val amount: Double
)