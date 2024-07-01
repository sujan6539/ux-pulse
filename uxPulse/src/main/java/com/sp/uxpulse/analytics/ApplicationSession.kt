package com.sp.uxpulse.analytics

import android.content.Context

/**
 * A session class that provides necessary values, here its application context, to the framework.
 */
interface ApplicationSession {
    fun getApplicationContext(): Context
}