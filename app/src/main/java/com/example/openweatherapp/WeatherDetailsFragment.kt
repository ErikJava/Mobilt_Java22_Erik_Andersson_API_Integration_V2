package com.example.openweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class WeatherDetailsFragment : Fragment() {
    private var temperatureTextView: TextView? = null
    private var cloudinessTextView: TextView? = null
    private var windTextView: TextView? = null
    private var lastUpdateTextView: TextView? = null // Add this TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_details, container, false)
        temperatureTextView = view.findViewById(R.id.temperatureTextView)
        cloudinessTextView = view.findViewById(R.id.cloudsTextView)
        windTextView = view.findViewById(R.id.windTextView)
        lastUpdateTextView = view.findViewById(R.id.lastUpdateTextView) // Find the TextView by ID

        // Retrieve the passed weather data and display it
        val bundle = arguments
        if (bundle != null) {
            val temperatureCelsius = bundle.getDouble("temperatureCelsius")
            val cloudiness = bundle.getString("cloudiness")
            val wind = bundle.getString("wind")
            val lastUpdate = bundle.getString("lastUpdate") // Get last update information

            // Display the weather data in the TextViews
            temperatureTextView?.setText("Temperature in Celsius: $temperatureCelsius°C")
            cloudinessTextView?.setText("Cloudiness: $cloudiness")
            windTextView?.setText("Wind: $wind")
            lastUpdateTextView?.setText("Last Update: $lastUpdate") // Display last update
        } else {
            // Handle the case where weather data is missing
            temperatureTextView?.setText("Temperature data not available")
            cloudinessTextView?.setText("Cloudiness data not available")
            windTextView?.setText("Wind data not available")
            lastUpdateTextView?.setText("Last Update data not available")
        }
        return view
    }
}