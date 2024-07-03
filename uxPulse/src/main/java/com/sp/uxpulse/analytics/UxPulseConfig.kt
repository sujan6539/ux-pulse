package com.sp.uxpulse.analytics

data class UxPulseConfig(
    var apiKey: String,
    var projectId: String,
    val version: String,
    val instanceId:String,
    var mTrackAutomaticEvents: Boolean = true,
    var mDisableAppOpenEvent: Boolean = false,
    val mSessionTimeoutDuration: Int = 0,
    var instantPushEvent: Boolean = false
)