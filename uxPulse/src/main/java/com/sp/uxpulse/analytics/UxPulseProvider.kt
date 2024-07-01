package com.sp.uxpulse.analytics

object UxPulseProvider {

    fun initialize(
        applicationSession: ApplicationSession,
        uxPulseConfig: UxPulseConfig,
        instanceName: String,
        optOutTrackingDefaultParams: Boolean = true,
        trackAutomaticEvents: Boolean = true,
    ): UxPulseTracker {
        return UxPulseTracker.getInstance(
            applicationSession,
            uxPulseConfig,
            optOutTrackingDefaultParams,
            instanceName,
            trackAutomaticEvents
        )
    }

}