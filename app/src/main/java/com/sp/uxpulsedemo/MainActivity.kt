package com.sp.uxpulsedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
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
            (applicationContext as MainApplication).usPulseTracker.trackClickEvent("Explore List", "Home")
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))

        }

        activityMainBinding.btnGoTo3.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.trackClickEvent("Checkout flow", "Home")
            startActivity(Intent(this@MainActivity, MainActivity3::class.java))

        }

        activityMainBinding.btnGoToProfile.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.trackClickEvent("Profile", "Home")
            startActivity(Intent(this@MainActivity, MainActivity1a::class.java))

        }

        activityMainBinding.btnStartSession.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.startSession()
        }

        activityMainBinding.btnStopSession.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.endSession()
        }

        (applicationContext as MainApplication).usPulseTracker.trackScreenViewEvent("Home", this::class.java.simpleName,)
    }
}