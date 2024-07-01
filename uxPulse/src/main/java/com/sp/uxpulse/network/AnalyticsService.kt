package com.sp.uxpulse.network

import com.sp.uxpulse.event.Event
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalyticsService {

    @POST("analytics/events")
    fun sendEvent(@Body event: Event): Call<Void>
}