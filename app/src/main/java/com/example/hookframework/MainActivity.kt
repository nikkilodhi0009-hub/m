package com.example.hookframework

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.framework.core.FrameworkCore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = findViewById<TextView>(R.id.status)
        status.text = "Android 10 LSPosed-style module scaffold\nFramework initialized"
        FrameworkCore.logInfo("MainActivity created")
    }
}
