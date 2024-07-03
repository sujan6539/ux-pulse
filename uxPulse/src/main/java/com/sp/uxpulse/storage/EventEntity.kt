package com.sp.uxpulse.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventName: String,
    val timestamp: String,
    val additionalContext: Map<String, Any> // Store as JSON string
)