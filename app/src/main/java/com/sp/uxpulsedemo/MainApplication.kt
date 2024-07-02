package com.sp.uxpulsedemo

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.sp.uxpulse.analytics.ApplicationSession
import com.sp.uxpulse.analytics.UxPulseConfig
import com.sp.uxpulse.analytics.UxPulseProvider
import com.sp.uxpulse.analytics.UxPulseTracker

class MainApplication : Application() {
    lateinit var usPulseTracker: UxPulseTracker

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        var applicationSession = object : ApplicationSession {
            override fun getApplicationContext(): Context {
                return applicationContext
            }

        }

        val uxPulseConfig =  UxPulseConfig(
            apiKey = "asdfadf@#asdffsdfa",
            projectId = applicationContext.packageName,
            version = "1.0.0"
        )

        FirebaseApp.initializeApp(this);
        usPulseTracker = UxPulseProvider.initialize(
            applicationSession,
            uxPulseConfig,
            instanceName = "asdkfjasdf"
        )
    }
}