package com.sp.uxpulse.network

import com.sp.uxpulse.storage.EventModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalyticsService {

    @POST("events.json")
    fun sendEvent(@Body event: List<EventModel>?): Call<Void?>
}