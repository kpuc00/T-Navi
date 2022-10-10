package dev.kstrahilov.tnavi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type


class StopFormActivity : AppCompatActivity() {
    private lateinit var stopIdRow: LinearLayout
    private lateinit var tvStopId: TextView
    private lateinit var etStopTitle: EditText
    private lateinit var selectedLocation: LatLng
    private var stop: Stop? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_form)

        val intent = intent
        try {
            stop = (intent.getParcelableExtra("stop") as Stop?)!!
        } catch (_: java.lang.NullPointerException) {
        }

        etStopTitle = findViewById(R.id.et_stop_title)

        if (stop != null) {
            title = application.getString(R.string.label_edit_stop)

            stopIdRow = findViewById(R.id.stop_id_row)
            tvStopId = findViewById(R.id.tv_stop_id)
            tvStopId.text = stop!!.id.toString()
            stopIdRow.visibility = View.VISIBLE

            etStopTitle.setText(stop!!.title)
            etStopTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    stop!!.title = etStopTitle.text.toString()
                }

            })
        } else {
            title = application.getString(R.string.label_create_stop)
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

        val gson = Gson()

        val path: String = applicationContext.filesDir.toString()
        val fileName = "/stops.json"
        val file = File(path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
                val readStops: ArrayList<Stop> = gson.fromJson(readJson, stopListType)
                readStops.addAll(stops)
                val jsonString: String = gson.toJson(readStops)
                file.writeText(jsonString, Charsets.UTF_8)
                checkIfDataWritten(file, jsonString, stops[0].title)
            } else {
                val jsonString: String = gson.toJson(stops)
                file.writeText(jsonString, Charsets.UTF_8)
                checkIfDataWritten(file, jsonString, stops[0].title)
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext, applicationContext.getString(R.string.error), Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    private fun checkIfDataWritten(file: File, jsonString: String, stopTitleToDisplay: String) {
        if (file.readText(Charsets.UTF_8) == jsonString) {
            Toast.makeText(
                applicationContext,
                "${applicationContext.getString(R.string.stop_)} $stopTitleToDisplay ${
                    applicationContext.getString(R.string.was_created)
                }",
                Toast.LENGTH_LONG
            ).show()
            finish()
        } else {
            Toast.makeText(
                applicationContext, applicationContext.getString(R.string.error), Toast.LENGTH_LONG
            ).show()
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