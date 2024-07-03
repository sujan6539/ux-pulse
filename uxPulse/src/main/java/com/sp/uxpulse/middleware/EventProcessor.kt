package com.sp.uxpulse.middleware

import DatabaseManager
import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sp.uxpulse.analytics.UxPulseConfig
import com.sp.uxpulse.mapToJsonObject
import com.sp.uxpulse.network.NetworkManager
import com.sp.uxpulse.payload.BasePayload
import com.sp.uxpulse.payload.DevicePayload
import com.sp.uxpulse.payload.TrackAction.Companion.KEY_EVENT
import com.sp.uxpulse.searchInMap
import com.sp.uxpulse.storage.EventEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class EventProcessor private constructor(
    private val appContext: Context,
    private val config: UxPulseConfig,
    private val devicePayload: DevicePayload,
) {

    private val databaseManager: DatabaseManager by lazy {
        DatabaseManager.getInstance(appContext)
    }

    fun processEvent(payload: BasePayload) {
        val event: Map<String, Any> = devicePayload.deviceMap + payload.map
        if (config.instantPushEvent) {
            pushEventToFirebase(event)
        } else {
            storeEventLocally(event)
            enqueueEvents()
        }
    }

    private fun pushEventToFirebase(event: Map<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            NetworkManager.dispatch(listOf(mapToJsonObject(event)))
        }
    }

    private fun storeEventLocally(event: Map<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            val eventName: String? = event.searchInMap(KEY_EVENT) as? String
            val timeStamp: String? = event.searchInMap(BasePayload.KEY_TIME_STAMP) as? String

            if (eventName.isNullOrBlank() || timeStamp.isNullOrBlank()) {
                Log.e(EventProcessor::class.simpleName, "Invalid event")
            } else {
                val eventEntity = EventEntity(
                    eventName = eventName,
                    timestamp = timeStamp,
                    additionalContext = event
                )
                databaseManager.insertEvent(eventEntity)
                enqueueEvents()
            }

        }
    }

    @WorkerThread
    private fun enqueueEvents() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<BatchEventWorker>(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "BatchEventDispatchWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }


    companion object {
        private var sInstances: MutableMap<String, EventProcessor> = mutableMapOf()

        fun getInstance(
            messageContext: Context,
            config: UxPulseConfig,
            devicePayload: DevicePayload,
        ): EventProcessor {
            synchronized(sInstances) {
                val appContext: Context = messageContext.applicationContext
                val ret: EventProcessor
                val instanceName: String = config.apiKey
                if (!sInstances.containsKey(instanceName)) {
                    ret = EventProcessor(appContext, config, devicePayload)
                    sInstances[instanceName] = ret
                } else {
                    ret = sInstances[instanceName]!!
                }
                return ret
            }
        }

    }
}