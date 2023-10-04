package com.sam.network_logger.helpers

import com.google.gson.GsonBuilder

fun String.prettyPrintJson(): String {
    return try {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonElement = gson.fromJson(this, com.google.gson.JsonElement::class.java)
        gson.toJson(jsonElement)
    } catch (e: Exception) {
        this // Return the original string if parsing or formatting fails
    }
}