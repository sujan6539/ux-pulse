package com.sp.uxpulse.middleware

import com.sp.uxpulse.event.Event
import com.sp.uxpulse.storage.EventModel
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

class DispatchQueue {
    private val queue: Queue<EventModel> = ConcurrentLinkedQueue()

    fun enqueue(item: EventModel) {
        queue.add(item)
    }

    fun dequeueBatch(batchSize: Int): List<EventModel> {
        val batch = mutableListOf<EventModel>()
        for (i in 0 until batchSize) {
            val event = queue.poll() ?: break
            batch.add(event)
        }
        return batch
    }

    fun isEmpty() = queue.isEmpty()
}
