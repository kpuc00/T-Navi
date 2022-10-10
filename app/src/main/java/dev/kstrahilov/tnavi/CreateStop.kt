package dev.kstrahilov.tnavi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import java.io.File


class CreateStop : AppCompatActivity() {
    private lateinit var etStopTitle: EditText
    private lateinit var selectedLocation: LatLng
    private var stop: Stop? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stop)
        title = application.getString(R.string.label_create_stop)
        val intent = intent
        try {
            stop = (intent.getParcelableExtra("stop") as Stop?)!!
        } catch (_: java.lang.NullPointerException) {
        }

        etStopTitle = findViewById(R.id.et_stop_title)

        if (stop != null) {
            etStopTitle.setText(stop!!.title)
            etStopTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    stop!!.title = etStopTitle.text.toString()
                }

            })
        }

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            if (stop != null) {
                googleMap.addMarker(
                    MarkerOptions().position(stop!!.location)
                        .icon(bitmapFromVector(applicationContext, R.drawable.spirka))
                        .anchor(0.5F, 0.5F)
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stop!!.location, 17F))
                addCircle(googleMap, stop!!.location)
            }

            googleMap.setOnMapClickListener {
                selectedLocation = it
                googleMap.clear()
                googleMap.addMarker(
                    MarkerOptions().position(selectedLocation)
                        .icon(bitmapFromVector(applicationContext, R.drawable.spirka))
                        .anchor(0.5F, 0.5F)
                )
                addCircle(googleMap, selectedLocation)
                if (stop != null) {
                    stop!!.location = it
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_save -> {
            saveStop()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun saveStop() {
        val stops: ArrayList<Stop> = ArrayList()
        if (stop != null) {
            stops.add(stop!!)
        } else {
            stops.add(Stop(title = etStopTitle.text.toString().trim(), location = selectedLocation))
        }

        val path = "stops.json"
        try {
            File(path).createNewFile()
            File(path).printWriter().use {
                val gson = Gson()
                val jsonString = gson.toJson(stops)
                it.write(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var circle: Circle? = null

    private fun addCircle(googleMap: GoogleMap, latLng: LatLng) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions().center(latLng).radius(50.0)
                .strokeColor(ContextCompat.getColor(this, R.color.blue))
        )
    }

//    fun addObject(path: String, name: String, value: String) {
//        val gson = Gson()
//        val reader = FileReader(File(path))
//        val type = object : TypeToken<Map<String, String>>() {}.type
//        println("Type: $type")
//        val existingJson = gson.fromJson<Map<String, String>>(JsonReader(reader), type)
//        println("Existing Json: $existingJson")
//        val newJsonMap = existingJson.plus(Pair(name, value))
//        FileWriter(File(path)).use { writer -> writer.write(gson.toJson(newJsonMap)) }
//    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}