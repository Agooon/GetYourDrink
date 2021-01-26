package com.example.getyourdrink.api

import com.beust.klaxon.Json


data class Ingredient(
    @Json(name = "Id") val id: Long,
    @Json(name = "Name") val name: String,
    @Json(name = "Type") val type: String,
    @Json(name = "Volume") val volume: Int,
    @Json(name = "AvgSize") val avgSize: Int,
    @Json(name = "Description") val description: String
)