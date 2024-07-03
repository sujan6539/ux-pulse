package com.sp.uxpulse.payload

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import com.sp.uxpulse.analytics.ApplicationSession
import com.sp.uxpulse.analytics.UxPulseConfig


class DevicePayload(
    private var applicationSession: ApplicationSession,
    private var uxPulseConfig: UxPulseConfig,
) {

    val deviceMap: MutableMap<String, Any> = mutableMapOf()

    init {
        addAppData(applicationSession.getApplicationContext())
        addDeviceData()
    }

    private fun addAppData(context: Context) {
        try {
            val packageManager: PackageManager = context.packageManager
            val packageInfo: PackageInfo =
                packageManager.getPackageInfo(context.packageName, 0)
            val app: MutableMap<String, Any> = mutableMapOf()
            app[KEY_APP_NAME] = packageInfo.applicationInfo.loadLabel(packageManager)

            app[KEY_APP_VERSION] = packageInfo.versionName
            app[KEY_APP_NAMESPACE] = packageInfo.packageName
            app[KEY_APP_BUILD] = packageInfo.versionCode.toString()
            deviceMap[KEY_APP] = app as Map<String, Any>
        } catch (e: PackageManager.NameNotFoundException) {
            // ignore
        }
    }

    private fun addDeviceData() {

        val mutableDeviceInfo: MutableMap<String, String> = mutableMapOf()
        mutableDeviceInfo["android_lib_version"] = uxPulseConfig.version
        mutableDeviceInfo[KEY_OS_NAME] = "Android"
        mutableDeviceInfo[KEY_OS_VERSION] =
            if (Build.VERSION.RELEASE == null) "UNKNOWN" else Build.VERSION.RELEASE
        mutableDeviceInfo[KEY_DEVICE_MANUFACTURER] =
            if (Build.MANUFACTURER == null) "UNKNOWN" else Build.MANUFACTURER
        mutableDeviceInfo[KEY_DEVICE_BRAND] = if (Build.BRAND == null) "UNKNOWN" else Build.BRAND
        mutableDeviceInfo[KEY_DEVICE_MODEL] = if (Build.MODEL == null) "UNKNOWN" else Build.MODEL

        try {
            val manager: PackageManager = applicationSession.getApplicationContext().packageManager
            val info: PackageInfo =
                manager.getPackageInfo(applicationSession.getApplicationContext().packageName, 0)
            mutableDeviceInfo[KEY_APP_VERSION] = info.versionName
            mutableDeviceInfo[KEY_APP_VERSION_CODE] = info.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("Exception getting app version name", e.toString())
        }

        deviceMap[KEY_OS] = mutableDeviceInfo as Map<String, String>

        // Screen data
        val screen: MutableMap<String, Any> = mutableMapOf()
        val displayMetrics: DisplayMetrics =
            applicationSession.getApplicationContext().resources.displayMetrics

        screen[KEY_SCREEN_DENSITY] = displayMetrics.density
        screen[KEY_SCREEN_HEIGHT] = displayMetrics.heightPixels
        screen[KEY_SCREEN_WIDTH] = displayMetrics.widthPixels
        deviceMap[KEY_SCREEN] = screen as Map<String, Any>
    }

    companion object {
        // App

        const val KEY_APP: String = "app"

        const val KEY_APP_NAME: String = "name"

        const val KEY_APP_VERSION: String = "version"
        const val KEY_APP_VERSION_CODE: String = "version_code"

        const val KEY_APP_NAMESPACE: String = "namespace"

        const val KEY_APP_BUILD: String = "build"

        // Device

        const val KEY_DEVICE: String = "device"

        // OS

        const val KEY_OS: String = "os"

        const val KEY_OS_NAME: String = "name"

        const val KEY_OS_VERSION: String = "version"
        const val KEY_DEVICE_MANUFACTURER: String = "device_manufacturer"
        const val KEY_DEVICE_BRAND: String = "device_brand"
        const val KEY_DEVICE_MODEL: String = "device_model"

        // Screen

        const val KEY_SCREEN: String = "screen"

        const val KEY_SCREEN_DENSITY: String = "density"

        const val KEY_SCREEN_HEIGHT: String = "height"

        const val KEY_SCREEN_WIDTH: String = "width"
    }
}