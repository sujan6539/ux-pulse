package com.sp.uxpulse.analytics

import DatabaseManager
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.sp.uxpulse.analytics.TimeZone.TIMESTAMP_TIMEZONE_OFFSET
import com.sp.uxpulse.breadcrumbs.Breadcrumb
import com.sp.uxpulse.breadcrumbs.BreadcrumbManager
import com.sp.uxpulse.event.Event
import com.sp.uxpulse.event.Event.Companion.CLICK_EVENT
import com.sp.uxpulse.event.Event.Companion.SCREEN_VIEW_EVENT
import com.sp.uxpulse.middleware.EventProcessor
import com.sp.uxpulse.session.SessionManager


class UxPulseTracker private constructor(
    applicationSession: ApplicationSession,
    var config: UxPulseConfig,
    var optOutTrackingDefaultParams: Boolean,
    var instanceName: String,
    var trackAutomaticEvents: Boolean,
) {

    private var deviceInfo: Map<String, String>
    private var sessionManager: SessionManager? = null
    private var eventProcessor: EventProcessor


    init {
        val mutableDeviceInfo: MutableMap<String, String> = mutableMapOf()
        mutableDeviceInfo["android_lib_version"] = config.version
        mutableDeviceInfo["device_os"] = "Android"
        mutableDeviceInfo["device_os_version"] =
            if (Build.VERSION.RELEASE == null) "UNKNOWN" else Build.VERSION.RELEASE
        mutableDeviceInfo["device_manufacturer"] =
            if (Build.MANUFACTURER == null) "UNKNOWN" else Build.MANUFACTURER
        mutableDeviceInfo["device_brand"] = if (Build.BRAND == null) "UNKNOWN" else Build.BRAND
        mutableDeviceInfo["device_model"] = if (Build.MODEL == null) "UNKNOWN" else Build.MODEL

        try {
            val manager: PackageManager = applicationSession.getApplicationContext().packageManager
            val info: PackageInfo =
                manager.getPackageInfo(applicationSession.getApplicationContext().packageName, 0)
            mutableDeviceInfo["android_app_version"] = info.versionName
            mutableDeviceInfo["android_app_version_code"] = info.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("Exception getting app version name", e.toString())
        }

        deviceInfo = mutableDeviceInfo
        initializeSessionManager(applicationSession);

        if (!config.instantPushEvent) {
            DatabaseManager.getInstance(applicationSession.getApplicationContext())
        }
        val timeZone = TimeZone.TIMESTAMP_TIMEZONE_OFFSET
        eventProcessor =
            EventProcessor.getInstance(applicationSession.getApplicationContext(), config)
        eventProcessor.processEvent(
            Event(
                AutomaticEvents.FIRST_OPEN,
                TIMESTAMP_TIMEZONE_OFFSET,
                mapOf()
            )
        )

    }


    fun startSession() {
        sessionManager?.startSession()
    }

    fun endSession() {
        sessionManager?.stopSession()
    }

    fun trackState(state: String, contextData: Map<String, String>?) {
        eventProcessor.processEvent(
            Event(
                state,
                TIMESTAMP_TIMEZONE_OFFSET,
                contextData ?: mapOf()
            )
        )

    }

    fun trackAction(action: String, contextData: Map<String, String>?) {

        eventProcessor.processEvent(
            Event(
                action,
                TIMESTAMP_TIMEZONE_OFFSET,
                contextData ?: mapOf()
            )
        )
    }

    fun trackScreenViewEvent(context: Context, screenName: String) {
        BreadcrumbManager.instance?.addBreadcrumb(
            Breadcrumb(
                context::class.java.simpleName,
                screenName
            )
        )

        val additionalContext = mapOf(
            "screenName" to screenName,
            "breadCrumb" to (BreadcrumbManager.instance?.getBreadcrumbs()
                ?.joinToString(separator = " | ") { it.screenName } ?:"")
        )

        val time = TIMESTAMP_TIMEZONE_OFFSET
        val event = Event(SCREEN_VIEW_EVENT, time, additionalContext)

        eventProcessor.processEvent(event)
    }

    fun trackClickEvent(buttonId: String, screenName: String) {
        val additionalContext = mapOf(
            "buttonId" to buttonId,
            "screenName" to screenName
        )

        val event = Event(CLICK_EVENT, TIMESTAMP_TIMEZONE_OFFSET, additionalContext)
        eventProcessor.processEvent(event)

    }

    private fun initializeSessionManager(applicationSession: ApplicationSession) {
        val app: Application? = applicationSession.getApplicationContext() as? Application
        sessionManager = SessionManager(this)
        app?.registerActivityLifecycleCallbacks(sessionManager)
    }


    companion object {

        @JvmStatic
        internal fun getInstance(
            applicationSession: ApplicationSession,
            uxPulseConfig: UxPulseConfig,
            optOutTrackingDefaultParams: Boolean,
            instanceName: String,
            trackAutomaticEvents: Boolean,
        ): UxPulseTracker {
            return UxPulseTracker(
                applicationSession,
                uxPulseConfig,
                optOutTrackingDefaultParams,
                instanceName,
                trackAutomaticEvents
            )
        }
    }

}