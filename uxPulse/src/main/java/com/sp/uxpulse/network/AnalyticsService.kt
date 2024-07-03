package com.sp.uxpulse.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalyticsService {

    @POST("events.json")
    fun sendEvent(@Body event: List<JsonObject>): Call<Void?>
}