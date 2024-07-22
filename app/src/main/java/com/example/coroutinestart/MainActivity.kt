package com.example.coroutinestart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
	private lateinit var tvLocation: TextView
	private lateinit var tvTemperature: TextView
	private lateinit var progressBar: ProgressBar
	private lateinit var loadButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		initViews()
		loadButton.setOnClickListener {
			synchronousCode()
		}
	}

	private fun initViews() {
		tvLocation = findViewById(R.id.tv_location)
		tvTemperature = findViewById(R.id.tv_temperature)
		progressBar = findViewById(R.id.progress)
		loadButton = findViewById(R.id.button_load)
	}

	private fun synchronousCode() {
		loadButton.isEnabled = false
		progressBar.visibility = View.VISIBLE
		Log.d("MainActivity", "Load started: $this")
		loadCity { city ->
			tvLocation.text = city
			loadTemperature(city) { temperature ->
				tvTemperature.text = temperature
				progressBar.visibility = View.GONE
				loadButton.isEnabled = true
				Log.d("MainActivity", "Load finished: $this")
			}
		}
	}

	private fun loadCity(callback: (String) -> Unit) {
		thread {
			Thread.sleep(5000L)
			runOnUiThread {
				callback("Almaty")
			}
		}
	}

	private fun loadTemperature(city: String, callback: (String) -> Unit) {
		thread {
			runOnUiThread {
				Toast.makeText(
					this,
					getString(R.string.loading_temperature_for_city, city), Toast.LENGTH_SHORT
				).show()
			}
			Thread.sleep(5000L)
			runOnUiThread {
				callback("35")
			}
		}
	}
}