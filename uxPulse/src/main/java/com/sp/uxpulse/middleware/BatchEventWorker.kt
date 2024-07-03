package com.sp.uxpulse.middleware

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sp.uxpulse.mapToJsonObject
import com.sp.uxpulse.network.NetworkManager
import com.sp.uxpulse.storage.AnalyticsDatabase
import com.sp.uxpulse.storage.EventModel

class BatchEventWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = AnalyticsDatabase.getDatabase(applicationContext)
        val eventDao = database.eventDao()
        val eventQueue = DispatchQueue()

        // Load events from the database to the queue
        val events = eventDao.getEvents(100) // For example, get 100 events at a time
        events.forEach { eventQueue.enqueue(it) }

        while (!eventQueue.isEmpty()) {
            val batch = eventQueue.dequeueBatch(20) // Process 20 events at a time

            // Send batch data to the server
            val success = sendBatchToServer(batch)
            if (success) {
                println("success")
                // Delete sent events from the database
                eventDao.deleteEvents(batch.map {
                    it.id
                })
            } else {
                println("failure")
                // Handle failure (e.g., retry later)
                return Result.retry()
            }
        }

        return Result.success()
    }


    private suspend fun sendBatchToServer(batch: List<EventModel>): Boolean {
        return NetworkManager.dispatch(batch.map { mapToJsonObject(it.additionalContext) })
    }
}