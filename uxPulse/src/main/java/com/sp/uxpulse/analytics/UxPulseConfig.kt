package com.sp.uxpulse.analytics

data class UxPulseConfig(
    var apiKey: String,
    var projectId: String,
    val version: String,
    var mTrackAutomaticEvents: Boolean = true,
    var mDisableAppOpenEvent: Boolean = false,
    val mSessionTimeoutDuration: Int = 0,
    var instantPushEvent: Boolean = false
)