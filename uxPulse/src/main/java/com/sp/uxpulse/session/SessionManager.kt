package com.sp.uxpulse.session

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.sp.uxpulse.analytics.AutomaticEvents
import com.sp.uxpulse.analytics.AutomaticEvents.SESSION_PAUSED_LENGTH
import com.sp.uxpulse.analytics.UxPulseTracker
import com.sp.uxpulse.breadcrumbs.BreadcrumbManager
import com.sp.uxpulse.session.SessionMetadata.Companion.CHECK_DELAY
import com.sp.uxpulse.session.SessionMetadata.Companion.mSessionStartEpoch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException

class SessionManager(private val tracker: UxPulseTracker) :
    Application.ActivityLifecycleCallbacks, FragmentManager.FragmentLifecycleCallbacks() {
    private var sessionMetadata: SessionMetadata? = null

    fun startSession() {
        if (sessionMetadata == null) {
            Log.d(SessionManager::class.java.simpleName, "Session Started.")
            sessionMetadata = SessionMetadata().apply {
                isSessionActive = true
                mIsForeground = true
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

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(this, true)
        }
    }

    override fun onActivityStarted(p0: Activity) {
        sessionMetadata?.apply {
            mIsForeground = true
        }
    }

    override fun onActivityResumed(p0: Activity) {
        if (sessionMetadata == null) {
            Log.d(SessionManager::class.java.simpleName, "Session tracking disable.")
            return
        }

        sessionMetadata?.apply {
            mPaused = false
            val wasBackground = !mIsForeground
            mIsForeground = true

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

        sessionMetadata?.apply {
            mPaused = true
            CoroutineScope(Dispatchers.IO).launch {
                delay(CHECK_DELAY)
                if (mIsForeground && mPaused) {
                    mIsForeground = false
                    if (isSessionActive) {
                        val sessionEndTime = System.currentTimeMillis()
                        val sessionDuration = sessionEndTime - mSessionStartEpoch
                        trackSessionDuration(SESSION_PAUSED_LENGTH, sessionDuration)
                    }

                }
            }

        }
    }


    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
        BreadcrumbManager.instance?.removeBreadcrumb(p0::class.simpleName);
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        BreadcrumbManager.instance?.removeBreadcrumb(f::class.simpleName)
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
