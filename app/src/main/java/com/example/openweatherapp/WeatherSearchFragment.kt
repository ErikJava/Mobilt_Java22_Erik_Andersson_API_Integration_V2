package com.example.openweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherSearchFragment : Fragment() {
    private var cityEditText: EditText? = null
    private var searchButton: Button? = null
    private var requestQueue: RequestQueue? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_search, container, false)
        cityEditText = view.findViewById(R.id.cityEditText)
        searchButton = view.findViewById(R.id.searchButton)
        requestQueue = Volley.newRequestQueue(requireContext())
        searchButton?.setOnClickListener(View.OnClickListener { // Handle search button click
            val city = cityEditText?.getText().toString()

            // Call the method to fetch weather data
            fetchWeatherData(city)
        })
        return view
    }

    private fun fetchWeatherData(cityName: String) {
        val apiKey = "9a2f9ac50129b5dad7ebb7aa0fb4d4ce" // Replace with your Geocoding API key
        val apiUrl =
            "https://api.openweathermap.org/geo/1.0/direct?q=$cityName&limit=5&appid=$apiKey"

        // Request to obtain latitude and longitude coordinates for the city
        val geocodingJsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apiUrl,
            null,
            { response ->
                try {
                    if (response != null && response.length() > 0) {
                        // Assuming you want to use the first result
                        val locationObject = response.getJSONObject(0)
                        val latitude = locationObject.getDouble("lat")
                        val longitude = locationObject.getDouble("lon")

                        // Now that you have the coordinates, call fetchWeatherDataWithCoordinates
                        fetchWeatherDataWithCoordinates(latitude, longitude)
                    } else {
                        // Handle the case where no results were found for the city
                        Toast.makeText(
                            requireContext(),
                            "No results found for the city",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON parsing error
                    Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        ) { error -> // Handle geocoding API call failure
            Toast.makeText(
                requireContext(),
                "Error fetching city coordinates: " + error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
        requestQueue!!.add(geocodingJsonArrayRequest)
    }

    private fun fetchWeatherDataWithCoordinates(latitude: Double, longitude: Double) {
        val apiKey = "9a2f9ac50129b5dad7ebb7aa0fb4d4ce" // Replace with your Weather API key
        val apiUrl =
            "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey"

        // Request to fetch weather data using coordinates
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiUrl,
            null,
            { response ->
                try {
                    // Parse the JSON response and extract weather data
                    val mainObject = response.getJSONObject("main")
                    val temperatureKelvin = mainObject.getDouble("temp")
                    val temperatureCelsius = temperatureKelvin - 273.15

                    // Extract the "last update" information
                    val lastUpdateTimestamp = response.getLong("dt")
                    val lastUpdate = formatLastUpdate(lastUpdateTimestamp)

                    // Extract cloudiness and wind information
                    val cloudiness =
                        response.getJSONArray("weather").getJSONObject(0).getString("description")
                    val wind = response.getJSONObject("wind").getString("speed")

                    // Pass the weather data to the WeatherDetailsFragment
                    val weatherDetailsFragment = WeatherDetailsFragment()
                    val bundle = Bundle()
                    bundle.putDouble("temperatureCelsius", temperatureCelsius)
                    bundle.putString("lastUpdate", lastUpdate) // Pass last update information
                    bundle.putString("cloudiness", cloudiness)
                    bundle.putString("wind", wind)
                    weatherDetailsFragment.arguments = bundle
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, weatherDetailsFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { // Handle API call failure
            Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT)
                .show()
        }
        requestQueue!!.add(jsonObjectRequest)
    }

    // Helper method to format the "last update" timestamp into a readable date and time
    private fun formatLastUpdate(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert timestamp to milliseconds
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }
}