package com.sp.uxpulsedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.sp.uxpulsedemo.databinding.ActivityMain4Binding

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val activityMain4Binding: ActivityMain4Binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets



        }
        (applicationContext as MainApplication).usPulseTracker.trackScreenViewEvent(this,"Check out")
        activityMain4Binding.btnGoTo.setOnClickListener {
            (applicationContext as MainApplication).usPulseTracker.trackClickEvent("Checkout Confirmed","Check out")
            startActivity(Intent(this@MainActivity4, MainActivity2::class.java).apply {
                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
        }
    }
}