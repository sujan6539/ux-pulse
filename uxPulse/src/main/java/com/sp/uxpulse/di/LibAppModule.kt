package com.sp.uxpulse.di

import android.content.Context
import com.sp.uxpulse.analytics.UxPulseConfig
import com.sp.uxpulse.middleware.EventProcessor
import com.sp.uxpulse.payload.DevicePayload
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LibAppModule(
    private val appContext: Context,
    private val uxPulseConfig: UxPulseConfig,
) {


    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return appContext
    }

    @Provides
    fun provideDeviceInfo(): DevicePayload {
        return DevicePayload(appContext, uxPulseConfig)
    }


    @Provides
    @Singleton
    fun provideEventProcessor(devicePayload: DevicePayload): EventProcessor {
        return EventProcessor.getInstance(appContext, uxPulseConfig, devicePayload)
    }
}