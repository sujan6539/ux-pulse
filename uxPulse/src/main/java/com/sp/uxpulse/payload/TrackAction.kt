package com.sp.uxpulse.payload

import java.util.UUID

class TrackAction(
    name: String,
    type: EventType,
    properties: Map<String, Any>,
    timestamp: String,
) : BasePayload(
    id = UUID.randomUUID().toString(),
    type, name, properties, timestamp
) {

    companion object {

        const val KEY_EVENT: String = "event"
    }

    init {
        map[KEY_EVENT] = (type as EventType.Track).actionType
        map[KEY_PROPERTIES] = properties
    }
}
