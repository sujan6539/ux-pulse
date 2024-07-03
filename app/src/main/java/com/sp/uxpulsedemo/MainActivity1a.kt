package com.sp.uxpulsedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.sp.uxpulsedemo.databinding.ActivityMainActivity1aBinding

class MainActivity1a : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val activityMainActivity1aBinding: ActivityMainActivity1aBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main_activity1a)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        (applicationContext as MainApplication).usPulseTracker.trackScreenViewEvent("Explore", this::class.java.simpleName,)

        activityMainActivity1aBinding.btnGoTo.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.trackClickEvent("Done","Explore")
            startActivity(Intent(this@MainActivity1a, MainActivity3::class.java))
        }
    }
}