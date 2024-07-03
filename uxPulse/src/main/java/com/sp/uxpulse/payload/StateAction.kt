package com.sp.uxpulse.payload

import java.util.UUID

class StateAction(
    type: EventType = EventType.ScreenView,
    name: String,
    properties: Map<String, Any>,
    timestamp: String,
) : BasePayload(
    id = UUID.randomUUID().toString(), type, name, properties, timestamp
) {

    companion object {

        const val KEY_EVENT: String = "event"
    }

    init {
        map[KEY_EVENT] = when(type) {
            EventType.ScreenView -> "screen_view"
            is EventType.State -> type.actionType
            is EventType.Track ->  type.actionType
        }
        map[KEY_NAME] = name
        map[KEY_PROPERTIES] = properties
    }
}