package com.sp.uxpulse.event

import org.json.JSONObject

data class Event(
    val eventName: String,
    val timestamp: String,
    val additionalContext: Map<String, String>,
) {

    // Method to convert Event object to JSON
    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("eventName", eventName)
        jsonObject.put("timestamp", timestamp)

        val contextJson = JSONObject()
        for ((key, value) in additionalContext) {
            contextJson.put(key, value)
        }
        jsonObject.put("additionalContext", contextJson)

        return jsonObject
    }

    companion object {

        const val CLICK_EVENT = "click_event"
        const val SCREEN_VIEW_EVENT = "screen_view_event"

        // Method to create Event object from JSON
        fun fromJson(jsonObject: JSONObject): Event {
            val eventName = jsonObject.getString("eventName")
            val timestamp = jsonObject.getString("timestamp")

            val contextJson = jsonObject.getJSONObject("additionalContext")
            val additionalContext = mutableMapOf<String, String>()
            contextJson.keys().forEach { key ->
                additionalContext[key] = contextJson.getString(key)
            }

            return Event(eventName, timestamp, additionalContext)
        }
    }
}
