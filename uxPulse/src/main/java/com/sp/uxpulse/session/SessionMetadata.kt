package com.sp.uxpulse.session

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.security.SecureRandom


class SessionMetadata {
    private var mEventsCounter: Long = 0
    private var mSessionID: String? = null
    private val mRandom: SecureRandom
    var foregroundActivityCount = 0
    var sessionPausedTime: Long = 0
    var isSessionActive = false
    var mPaused = true
    var mIsForeground = false

    /* package */
    init {
        initSession()
        mRandom = SecureRandom()
    }

    private fun initSession() {
        mEventsCounter = 0L
        mSessionID = java.lang.Long.toHexString(SecureRandom().nextLong())
        mSessionStartEpoch = System.currentTimeMillis() / 1000
    }

    val metadataForEvent: JSONObject
        get() = getNewMetadata()


    private fun getNewMetadata(): JSONObject {
        val metadataJson = JSONObject()
        try {
            metadataJson.put(KEY_EVENT_ID, java.lang.Long.toHexString(mRandom.nextLong()))
            metadataJson.put(KEY_SESSION_ID, mSessionID)
            metadataJson.put(KEY_SESSION_START_ID, mSessionStartEpoch)
            mEventsCounter++
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e("Cannot create session metadata JSON object", e.toString())
        }

        return metadataJson
    }

    companion object {
        const val KEY_EVENT_ID = "event_id"
        const val KEY_SESSION_ID = "session_id"
        const val KEY_SESSION_START_ID = "session_start_sec"
        var mSessionStartEpoch: Long = 0

        const val CHECK_DELAY: Long = 500L
    }
}