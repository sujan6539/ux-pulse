package com.sp.uxpulse.analytics

import DatabaseManager
import android.app.Application
import com.sp.uxpulse.breadcrumbs.Breadcrumb
import com.sp.uxpulse.breadcrumbs.BreadcrumbManager
import com.sp.uxpulse.middleware.EventProcessor
import com.sp.uxpulse.payload.DevicePayload
import com.sp.uxpulse.payload.EventType
import com.sp.uxpulse.payload.StateAction
import com.sp.uxpulse.payload.TrackAction
import com.sp.uxpulse.session.SessionManager
import com.sp.uxpulse.utils.TimeZone.TIMESTAMP_TIMEZONE_OFFSET


class UxPulseTracker private constructor(
    applicationSession: ApplicationSession,
    config: UxPulseConfig,
) {

    private var sessionManager: SessionManager? = null
    private var eventProcessor: EventProcessor
    private var devicePayload: DevicePayload


    init {

        initializeSessionManager(applicationSession);

        if (!config.instantPushEvent) {
            DatabaseManager.getInstance(applicationSession.getApplicationContext())
        }
        devicePayload = DevicePayload(applicationSession, config)

        eventProcessor =
            EventProcessor.getInstance(
                applicationSession.getApplicationContext(),
                config,
                devicePayload
            )
        eventProcessor.processEvent(
            StateAction(
                type = EventType.Track(AutomaticEvents.FIRST_OPEN),
                name = AutomaticEvents.FIRST_OPEN,
                properties = mapOf(),
                timestamp = TIMESTAMP_TIMEZONE_OFFSET,
            )
        )

    }


    fun startSession() {
        sessionManager?.startSession()
    }

    fun endSession() {
        sessionManager?.stopSession()
    }

    fun trackState(name: String, contextData: Map<String, String>?) {
        val stateAction = StateAction(
            type = EventType.State(name),
            name,
            contextData ?: mapOf(),
            TIMESTAMP_TIMEZONE_OFFSET,
        )
        eventProcessor.processEvent(stateAction)
    }

    fun trackAction(action: String, type: EventType, contextData: Map<String, Any>?) {
        val data = TrackAction(
            type = type,
            name = action,
            properties = contextData ?: mapOf(),
            timestamp = TIMESTAMP_TIMEZONE_OFFSET,
        )
        eventProcessor.processEvent(data)
    }

    fun trackScreenViewEvent(
        screenName: String,
        screenTag: String,
    ) {
        BreadcrumbManager.instance?.addBreadcrumb(
            Breadcrumb(
                screenTag,
                screenName
            )
        )

        val additionalContext = mapOf(
            "screen_name" to screenName,
            "bread_crumb" to (BreadcrumbManager.instance?.getBreadcrumbs()
                ?.joinToString(separator = " | ") { it.screenName } ?: "")
        )

        val stateAction = StateAction(
            name = screenName,
            properties = additionalContext,
            timestamp = TIMESTAMP_TIMEZONE_OFFSET,
        )
        eventProcessor.processEvent(stateAction)
    }

    fun trackClickEvent(buttonId: String, screenName: String) {
        val additionalContext = mapOf(
            "button_name" to buttonId,
            "screen_name" to screenName
        )

        val data = TrackAction(
            type = EventType.Track("on_click"),
            name = buttonId,
            properties = additionalContext,
            timestamp = TIMESTAMP_TIMEZONE_OFFSET
        )

        eventProcessor.processEvent(data)

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
        ): UxPulseTracker {
            return UxPulseTracker(
                applicationSession,
                uxPulseConfig,
            )
        }
    }

}