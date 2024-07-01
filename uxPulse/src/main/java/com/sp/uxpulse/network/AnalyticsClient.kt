package com.sp.uxpulse.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AnalyticsClient {

    private const val BASE_URL = "https://uxpulse-7a742-default-rtdb.firebaseio.com/"
    private const val MAX_RETRIES = 3 // Configurable retry attempts

    private val retryInterceptor = RetryInterceptor(MAX_RETRIES)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(retryInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val analyticsService: AnalyticsService by lazy {
        retrofit.create(AnalyticsService::class.java)
    }
}