package com.example.openweatherapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var beginButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the "Begin" button
        beginButton = findViewById(R.id.beginButton)

        // Set a click listener for the "Begin" button
        beginButton?.setOnClickListener(View.OnClickListener {
            beginButton?.setVisibility(View.INVISIBLE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WelcomeFragment())
                .addToBackStack(null)
                .commit()
        })
    }
}