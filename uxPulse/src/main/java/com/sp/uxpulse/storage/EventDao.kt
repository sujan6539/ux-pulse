package com.sp.uxpulse.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Query("SELECT * FROM events LIMIT :limit")
    suspend fun getEvents(limit: Int): List<EventModel>

    @Query("DELETE FROM events WHERE id IN (:ids)")
    suspend fun deleteEvents(ids: List<Int>)
}

data class EventModel(
    val id: Int,
    val eventName: String,
    val timestamp: String,
    val additionalContext: String,
)