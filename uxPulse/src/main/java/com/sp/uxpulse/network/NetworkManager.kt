package com.sp.uxpulse.network

import com.google.gson.JsonObject
import kotlinx.coroutines.CompletableDeferred
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object NetworkManager {

    suspend fun dispatch(batch: List<JsonObject>): Boolean {
        val service = AnalyticsClient.analyticsService.sendEvent(batch)
        val deferred = CompletableDeferred<Boolean>()
        service.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    deferred.complete(true)
                } else {
                    deferred.complete(false)
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                deferred.complete(false)
            }
        })
        return deferred.await()
    }
}