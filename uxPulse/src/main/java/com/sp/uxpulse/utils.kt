package com.sp.uxpulse

import com.google.gson.JsonArray
import com.google.gson.JsonObject

fun Map<String, Any>.searchInMap(key: String): Any? {
    // Check if the key exists in the current map
    this[key]?.let { return it }

    // If not found, iterate through the map to check for nested maps
    for ((_, value) in this) {
        if (value is Map<*, *>) {
            // Safe cast to Map<String, Any> and recurse
            @Suppress("UNCHECKED_CAST")

            val result = (value as? Map<String, Any>)?.searchInMap(key)
            if (result != null) {
                return result
            }
        }
    }
    // Return null if the key is not found
    return null
}

@Synchronized
fun mapToJsonObject(map: Map<*, *>): JsonObject {
    val jsonObject = JsonObject()

    for ((k, value) in map) {
        val key = k as String
        when (value) {
            is String -> jsonObject.addProperty(key, value)
            is Number -> jsonObject.addProperty(key, value)
            is Boolean -> jsonObject.addProperty(key, value)
            is Map<*, *> -> {
                // Recursively convert nested map
                val nestedJsonObject = mapToJsonObject(value as Map<*, *>)
                jsonObject.add(key, nestedJsonObject)
            }
            is List<*> -> {
                // Handle list of values
                val jsonArray = JsonArray()
                for (element in value) {
                    when (element) {
                        is String -> jsonArray.add(element)
                        is Number -> jsonArray.add(element)
                        is Boolean -> jsonArray.add(element)
                        is Map<*, *> -> {
                            // Recursively convert nested map within list
                            val nestedJsonObject = mapToJsonObject(element as Map<String, Any>)
                            jsonArray.add(nestedJsonObject)
                        }
                        else -> {
                            // Handle other types as needed
                        }
                    }
                }
                jsonObject.add(key, jsonArray)
            }
            else -> {
                // Handle other types as needed
            }
        }
    }

    return jsonObject
}
