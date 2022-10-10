package dev.kstrahilov.tnavi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class CreateStop : AppCompatActivity() {
    private lateinit var etStopTitle: EditText
    private lateinit var selectedLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stop)
        title = application.getString(R.string.label_create_stop)

        etStopTitle = findViewById(R.id.et_stop_title)

        val testStop = Stop(title = "8-ми декември", location = LatLng(43.203468, 27.900332))

        etStopTitle.setText(testStop.title)

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.addMarker(
                MarkerOptions().position(testStop.location)
                    .icon(bitmapFromVector(applicationContext, R.drawable.spirka))
                    .anchor(0.5F, 0.5F)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(testStop.location, 17F))
            addCircle(googleMap, testStop.location)

            googleMap.setOnMapClickListener {
                selectedLocation = it
                googleMap.clear()
                googleMap.addMarker(
                    MarkerOptions().position(selectedLocation)
                        .icon(bitmapFromVector(applicationContext, R.drawable.spirka))
                        .anchor(0.5F, 0.5F)
                )
                addCircle(googleMap, selectedLocation)
                testStop.location = it
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_save -> {
            Toast.makeText(applicationContext, "save button", Toast.LENGTH_SHORT).show()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private var circle: Circle? = null

    private fun addCircle(googleMap: GoogleMap, latLng: LatLng) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(50.0)
                .strokeColor(ContextCompat.getColor(this, R.color.blue))
        )
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}