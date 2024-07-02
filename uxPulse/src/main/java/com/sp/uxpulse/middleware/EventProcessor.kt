package com.sp.uxpulse.middleware

import DatabaseManager
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.sp.uxpulse.analytics.UxPulseConfig
import com.sp.uxpulse.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit


class EventProcessor private constructor(private val appContext: Context, private val config: UxPulseConfig) {

    private val databaseManager: DatabaseManager by lazy {
        DatabaseManager.getInstance(appContext)
    }

    private val serverDB: FirebaseDatabase by lazy {
        Firebase.database
    }


    fun processEvent(event: Event) {
        if (config.instantPushEvent) {
            pushEventToFirebase(event)
        } else {
            storeEventLocally(event)
            startBatchOperation()
        }
    }

    private fun pushEventToFirebase(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val databaseReference = serverDB.reference.child("events").push()
                databaseReference.setValue(event).await()
                // Log success or handle it as necessary
            } catch (e: Exception) {
                // Handle failure
                e.printStackTrace()
            }
        }
    }

    private fun storeEventLocally(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            databaseManager.insertEvent(event)
        }
        startBatchOperation()
    }


    @WorkerThread
    private fun startBatchOperation() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<BatchEventWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "BatchEventDispatchWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }


    companion object {
        private var sInstances: MutableMap<String, EventProcessor> = mutableMapOf()

        fun getInstance(messageContext: Context, config: UxPulseConfig): EventProcessor {
            synchronized(sInstances) {
                val appContext: Context = messageContext.applicationContext
                val ret: EventProcessor
                val instanceName: String = config.apiKey
                if (!sInstances.containsKey(instanceName)) {
                    ret = EventProcessor(appContext, config)
                    sInstances[instanceName] = ret
                } else {
                    ret = sInstances[instanceName]!!
                }
                return ret
            }
        }

    }
}