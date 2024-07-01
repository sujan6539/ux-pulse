package com.sp.uxpulse.network

import com.sp.uxpulse.event.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkManager {


    fun dispatch(event: Event){
        val call = AnalyticsClient.analyticsService.sendEvent(event)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle success
                } else {
                    // Handle failure
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network failure
            }
        })
    }
}