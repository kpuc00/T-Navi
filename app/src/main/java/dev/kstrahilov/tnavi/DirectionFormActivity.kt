package dev.kstrahilov.tnavi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DirectionFormActivity : AppCompatActivity(), OnItemClickListener {
    private var lineId: String? = null
    private var direction: Direction? = null
    private lateinit var etDirectionTitle: EditText
    private lateinit var lvDirectionStops: ListView
    private lateinit var btnChooseAudio: Button
    private lateinit var btnPickStop: Button
    private lateinit var audioFileRow: LinearLayout
    private lateinit var tvSelectedAudioFile: TextView
    private lateinit var tvEmptyListStops: TextView
    private var selectedAudioFileName: String? = null
    private var selectedFile: Uri? = null
    private var route: ArrayList<Stop> = ArrayList()
    private lateinit var adapter: ArrayAdapter<Stop>
    private val operations = Operations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction_form)

        val intent = intent
        try {
            lineId = intent.getStringExtra("lineId")
            direction = (intent.getParcelableExtra("direction") as Direction?)!!
        } catch (_: java.lang.NullPointerException) {
        }

        etDirectionTitle = findViewById(R.id.et_direction_title)
        btnPickStop = findViewById(R.id.btn_pick_stop)
        btnPickStop.setOnClickListener {
            addStopToRoute()
        }
        btnChooseAudio = findViewById(R.id.btn_choose_audio_direction)
        audioFileRow = findViewById(R.id.audio_direction_file_row)
        tvSelectedAudioFile = findViewById(R.id.tv_selected_audio_direction_file)
        tvSelectedAudioFile.isSelected = true
        tvEmptyListStops = findViewById(R.id.tv_empty_list_stops)

        if (direction != null) {
            title = application.getString(R.string.label_edit_direction)

            etDirectionTitle.setText(direction!!.title)
            etDirectionTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    direction!!.title = etDirectionTitle.text.toString().trim()
                }

            })

            selectedAudioFileName = direction!!.announcementFileName
            if (direction!!.announcementFileName != "none") {
                tvSelectedAudioFile.text = direction!!.announcementFileName
                audioFileRow.visibility = View.VISIBLE
            }
        } else {
            title = application.getString(R.string.label_create_direction)
        }

        lvDirectionStops = findViewById(R.id.lv_direction_stops)
        if (direction != null) {
            route.addAll(
                operations.convertListOfStopIdsToListOfStops(
                    direction!!.routeStopIds, applicationContext
                )
            )
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, route)
        lvDirectionStops.adapter = adapter
        lvDirectionStops.onItemClickListener = this

        tvEmptyListStops.visibility = if (route.size < 1) {
            View.VISIBLE
        } else {
            View.GONE
        }

        btnChooseAudio.setOnClickListener {
            openFilePicker()
        }
    }

    private fun saveDirection() {
        val newDirection: Direction
        if (direction != null && etDirectionTitle.text.toString().trim() != "") {
            direction!!.routeStopIds.clear()
            direction!!.routeStopIds.addAll(operations.convertListOfStopsToListOfStopIds(route))
            newDirection = direction!!
        } else {
            if (etDirectionTitle.text.toString().trim() == "") {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(application.getString(R.string.error))
                alert.setMessage(application.getString(R.string.form_incomplete))
                alert.setNegativeButton(application.getString(R.string.ok)) { _, _ -> }
                val alertDialog = alert.create()
                alertDialog.show()
                return
            }
            newDirection = Direction(
                title = etDirectionTitle.text.toString().trim(),
                routeStopIds = operations.convertListOfStopsToListOfStopIds(route)
            )

        }
        operations.saveDirection(
            applicationContext, lineId, direction, newDirection, selectedFile, selectedAudioFileName
        )
        finish()
    }

    private fun addStopToRoute() {
        val stopsList = operations.loadStopsFromInternalStorage(applicationContext)
        val builder = AlertDialog.Builder(this)
        val stopsTitles = stopsList.map { it.title }.toTypedArray()
        builder.setTitle(application.getString(R.string.pick_stops))
        builder.setItems(stopsTitles) { _, position ->
            route.add(stopsList.find { it.title == stopsList[position].title }!!)
            lvDirectionStops.invalidateViews()
            tvEmptyListStops.visibility = if (route.size < 1) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (direction != null) {
            menuInflater.inflate(R.menu.menu_delete, menu)
        }
        menuInflater.inflate(R.menu.menu_save, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_save -> {
            saveDirection()

            true
        }
        R.id.action_delete -> {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(application.getString(R.string.action_delete))
            alert.setMessage(application.getString(R.string.direction_delete))
            alert.setPositiveButton(application.getString(R.string.yes)) { _, _ ->
                direction?.let { operations.deleteDirection(applicationContext, lineId, it) }
                finish()
            }
            alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
            val alertDialog = alert.create()
            alertDialog.show()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val stop = adapter.getItem(position)
        val alert = AlertDialog.Builder(this)
        alert.setTitle(stop!!.title)
        alert.setMessage(application.getString(R.string.remove_stop_from_route))
        alert.setPositiveButton(application.getString(R.string.yes)) { _, _ ->
            route.remove(stop)
            lvDirectionStops.invalidateViews()
            tvEmptyListStops.visibility = if (route.size < 1) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
        val alertDialog = alert.create()
        alertDialog.show()
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