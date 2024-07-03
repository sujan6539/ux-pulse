package com.sp.uxpulse.analytics

object UxPulseProvider {

    fun initialize(
        applicationSession: ApplicationSession,
        uxPulseConfig: UxPulseConfig,
    ): UxPulseTracker {
        return UxPulseTracker.getInstance(
            applicationSession,
            uxPulseConfig,
        )
    }

}