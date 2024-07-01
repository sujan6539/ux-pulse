package com.sp.uxpulse.session

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.sp.uxpulse.analytics.AutomaticEvents
import com.sp.uxpulse.analytics.AutomaticEvents.SESSION_PAUSED_LENGTH
import com.sp.uxpulse.analytics.UxPulseTracker
import com.sp.uxpulse.session.SessionMetadata.Companion.CHECK_DELAY
import com.sp.uxpulse.session.SessionMetadata.Companion.mSessionStartEpoch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException

class SessionManager(private val tracker: UxPulseTracker) :
    Application.ActivityLifecycleCallbacks {
    private var sessionMetadata: SessionMetadata? = null

    fun startSession() {
        if (sessionMetadata == null) {
            Log.d(SessionManager::class.java.simpleName, "Session Started.")
            sessionMetadata = SessionMetadata().apply {
                isSessionActive = true
            }
        }
    }

    fun stopSession() {
        sessionMetadata?.apply {
            Log.d(SessionManager::class.java.simpleName, "Session Stopped.")
            isSessionActive = false
            val sessionEndTime = System.currentTimeMillis()
            val sessionDuration = sessionEndTime - mSessionStartEpoch
            trackSessionDuration(AutomaticEvents.SESSION_LENGTH, sessionDuration)
            sessionMetadata = null
        }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityStarted(p0: Activity) {}

    override fun onActivityResumed(p0: Activity) {
        if (sessionMetadata == null) {
            Log.d(SessionManager::class.java.simpleName, "Session tracking disable.")
            return
        }

        sessionMetadata?.let { nonNullSessionMetaData ->
            nonNullSessionMetaData.mPaused = false
            val wasBackground = !nonNullSessionMetaData.mIsForeground
            nonNullSessionMetaData.mIsForeground = true

            if (wasBackground) {
                // App is in foreground now
                mSessionStartEpoch = System.currentTimeMillis()
            }

        }
    }

    override fun onActivityPaused(p0: Activity) {
        if (sessionMetadata == null) {
            Log.d(SessionManager::class.java.simpleName, "Session tracking disable.")
            return
        }

        sessionMetadata?.let { nonNullSessionMetaData ->
            nonNullSessionMetaData.mPaused = false
            CoroutineScope(Dispatchers.IO).launch {
                delay(CHECK_DELAY)
                if (nonNullSessionMetaData.mIsForeground && nonNullSessionMetaData.mPaused) {
                    nonNullSessionMetaData.mIsForeground = false
                    if (nonNullSessionMetaData.isSessionActive) {
                        val sessionEndTime = System.currentTimeMillis()
                        val sessionDuration = sessionEndTime - mSessionStartEpoch
                        trackSessionDuration(SESSION_PAUSED_LENGTH, sessionDuration)
                    }

                }
            }

        }
    }


    override fun onActivityStopped(p0: Activity) {
        sessionMetadata?.let { nonNullSessionMetaData ->
            if (!nonNullSessionMetaData.mIsForeground) {
                // App goes to background
                nonNullSessionMetaData.sessionPausedTime = System.currentTimeMillis()
                val sessionDuration = nonNullSessionMetaData.sessionPausedTime - mSessionStartEpoch
                // Send session duration to analytics
                trackSessionDuration("", sessionDuration)
            }

        }
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    private fun trackSessionDuration(sessionProperty: String, duration: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sessionLengthInSeconds = duration / 1000.0
                val sessionLengthRounded = (Math.round(sessionLengthInSeconds * 10.0) / 10.0)
                val sessionProperties = mapOf(
                    sessionProperty to sessionLengthRounded.toString()
                )
                tracker.trackState(sessionProperty, sessionProperties)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}
