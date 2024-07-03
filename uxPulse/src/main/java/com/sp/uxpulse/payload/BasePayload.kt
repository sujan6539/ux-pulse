package com.sp.uxpulse.payload

open class BasePayload(
    val id: String,
    var type: EventType,
    val name: String,
    var properties: Map<String, Any>,
    var timestamp: String,
) {
    val map: MutableMap<String, Any> = mutableMapOf()

    init {
        map[KEY_TIME_STAMP] = timestamp
    }

    companion object {
        const val KEY_NAME: String = "name"
        const val KEY_PROPERTIES: String = "properties"
        const val KEY_TIME_STAMP: String = "time_stamp"
    }
}