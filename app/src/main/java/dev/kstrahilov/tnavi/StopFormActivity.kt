package dev.kstrahilov.tnavi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class StopFormActivity : AppCompatActivity() {
    private lateinit var stopIdRow: LinearLayout
    private lateinit var tvStopId: TextView
    private lateinit var etStopTitle: EditText
    private lateinit var btnChooseAudio: Button
    private lateinit var selectedLocation: LatLng
    private var stop: Stop? = null
    private var operations = Operations()
    private var selectedAudioFileName: String? = null
    private var selectedFile: Uri? = null
    private lateinit var audioFileRow: LinearLayout
    private lateinit var tvSelectedAudioFile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_form)

        val intent = intent
        try {
            stop = (intent.getParcelableExtra("stop") as Stop?)!!
        } catch (_: java.lang.NullPointerException) {
        }

        etStopTitle = findViewById(R.id.et_stop_title)
        btnChooseAudio = findViewById(R.id.btn_choose_audio_stop)

        audioFileRow = findViewById(R.id.audio_stop_file_row)
        tvSelectedAudioFile = findViewById(R.id.tv_selected_audio_stop_file)
        tvSelectedAudioFile.isSelected = true

        if (stop != null) {
            title = application.getString(R.string.label_edit_stop)

            stopIdRow = findViewById(R.id.stop_id_row)
            tvStopId = findViewById(R.id.tv_stop_id)
            tvStopId.text = stop!!.id.toString()
            stopIdRow.visibility = View.VISIBLE

            selectedAudioFileName = stop!!.announcementFileName
            if (stop!!.announcementFileName != "none") {
                tvSelectedAudioFile.text = stop!!.announcementFileName
                audioFileRow.visibility = View.VISIBLE
            }

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

        btnChooseAudio.setOnClickListener {
            openFilePicker()
        }

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map
        ) as? SupportMapFragment

        try {
            mapFragment?.getMapAsync { googleMap ->
                googleMap.isMyLocationEnabled = true
                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                if (stop != null) {
                    googleMap.addMarker(
                        MarkerOptions().position(stop!!.location)
                            .icon(bitmapFromVector(applicationContext, R.drawable.stop))
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
                            .icon(bitmapFromVector(applicationContext, R.drawable.stop))
                            .anchor(0.5F, 0.5F)
                    )
                    addCircle(googleMap, selectedLocation)
                    if (stop != null) {
                        stop!!.location = it
                    }
                }
            }
        } catch (e: NotFoundException) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_map),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (stop != null) {
            menuInflater.inflate(R.menu.menu_delete, menu)
        }
        menuInflater.inflate(R.menu.menu_save, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_save -> {
            saveStop()

            true
        }
        R.id.action_delete -> {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(application.getString(R.string.action_delete))
            alert.setMessage(application.getString(R.string.stop_delete))
            alert.setPositiveButton(application.getString(R.string.yes)) { _, _ ->
                operations.deleteStop(applicationContext, stop)
                finish()
            }
            alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
            val alertDialog = alert.create()
            alertDialog.show()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun saveStop() {
        val stops: ArrayList<Stop> = ArrayList()
        if (stop != null && etStopTitle.text.toString() != "") {
            stops.add(stop!!)
        } else {
            if (!this::selectedLocation.isInitialized || etStopTitle.text.toString() == "") {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(application.getString(R.string.error))
                alert.setMessage(application.getString(R.string.form_incomplete))
                alert.setNegativeButton(application.getString(R.string.ok)) { _, _ -> }
                val alertDialog = alert.create()
                alertDialog.show()
                return
            }
            stops.add(Stop(title = etStopTitle.text.toString().trim(), location = selectedLocation))
        }
        operations.saveStop(
            applicationContext,
            stop,
            stops,
            selectedFile,
            selectedAudioFileName
        )
        finish()

    }

    private var circle: Circle? = null

    private fun addCircle(googleMap: GoogleMap, latLng: LatLng) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions().center(latLng).radius(50.0)
                .strokeColor(ContextCompat.getColor(this, R.color.green))
        )
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0, 0, 70, 70
        )
        val bitmap = Bitmap.createBitmap(
            70, 70, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                if (result.data != null) {
                    selectedFile = result.data!!.data!!
                    selectedAudioFileName =
                        operations.getFileNameFromUri(applicationContext, selectedFile).toString()
                    tvSelectedAudioFile.text = selectedAudioFileName
                    audioFileRow.visibility = View.VISIBLE
                }
            } else {
                return@registerForActivityResult
            }
        }
}