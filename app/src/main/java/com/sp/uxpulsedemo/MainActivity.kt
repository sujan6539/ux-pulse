package com.sp.uxpulsedemo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.sp.uxpulse.analytics.ApplicationSession
import com.sp.uxpulse.analytics.UxPulseConfig
import com.sp.uxpulse.analytics.UxPulseProvider
import com.sp.uxpulse.analytics.UxPulseTracker
import com.sp.uxpulsedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        activityMainBinding.btnGoTo.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.trackClickEvent("go to second clicked", "main Activity")
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))

        }

        activityMainBinding.btnStartSession.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.startSession()
        }

        activityMainBinding.btnStopSession.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.endSession()
        }

        (applicationContext as MainApplication).usPulseTracker.trackScreenViewEvent("main Activity")
    }
}