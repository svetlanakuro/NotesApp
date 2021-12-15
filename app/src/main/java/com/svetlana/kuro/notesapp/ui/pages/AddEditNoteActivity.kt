package com.svetlana.kuro.notesapp.ui.pages

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.databinding.ActivityAddNoteBinding
import com.svetlana.kuro.notesapp.ui.main.MainActivity
import com.svetlana.kuro.notesapp.utils.AsyncGeocoder

private const val GPS_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

class AddEditNoteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_LOCATION = "EXTRA_LOCATION"
    }

    private val binding: ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(
            layoutInflater
        )
    }

    private var mapView: GoogleMap? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            binding.textViewLocation.isVisible = isGranted
        }

    private var location: Location? = null
        set(value) {
            field = value

            value?.let {
                val latlon = LatLng(it.latitude, it.longitude)
                mapView?.addMarker(MarkerOptions().position(latlon))
                mapView?.moveCamera(CameraUpdateFactory.newLatLng(latlon))
            }
        }

    private val locationManager by lazy { getSystemService(LOCATION_SERVICE) as LocationManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            binding.textViewLocation.text = intent.getStringExtra(EXTRA_LOCATION)

            MainActivity.NoteAnalytics.logEvent(this, "AddEditNoteActivity: Edit Note")
        } else {
            title = "Add Note"
            binding.textViewLocation.setText(R.string.click_note_location)
            MainActivity.NoteAnalytics.logEvent(this, "AddEditNoteActivity: Add Note")
        }

        permissionLauncher.launch(GPS_PERMISSION)

        binding.textViewLocation.setOnClickListener {
            if (checkSelfPermission(GPS_PERMISSION) != PERMISSION_GRANTED) return@setOnClickListener
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.run {
                AsyncGeocoder(this@AddEditNoteActivity).getFromLocation(
                    latitude,
                    longitude,
                    1
                ) { address: String? ->
                    binding.textViewLocation.text = address
                }
            }

            registerMapCallback {
                mapView = it
                Toast.makeText(this@AddEditNoteActivity, "Map Ready", Toast.LENGTH_SHORT).show()
                val sydney = LatLng(-34.0, 151.0)
                it.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                it.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        }

    }

    private fun registerMapCallback(callback: OnMapReadyCallback) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        if (binding.editTextTitle.text.toString().trim()
                .isBlank() || binding.editTextDescription.text.toString().trim().isBlank()
        ) {
            Toast.makeText(this, "Can not insert empty note!", Toast.LENGTH_SHORT).show()
            MainActivity.NoteAnalytics.logEvent(
                this,
                "AddEditNoteActivity: Can not insert empty note!"
            )
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_TITLE, binding.editTextTitle.text.toString())
            putExtra(EXTRA_DESCRIPTION, binding.editTextDescription.text.toString())
            putExtra(EXTRA_LOCATION, binding.textViewLocation.text)
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}