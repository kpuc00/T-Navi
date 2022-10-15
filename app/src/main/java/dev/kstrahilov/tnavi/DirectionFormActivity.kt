package dev.kstrahilov.tnavi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DirectionFormActivity : AppCompatActivity(), OnItemClickListener {
    private var lineId: String? = null
    private var direction: Direction? = null
    private lateinit var etDirectionTitle: EditText
    private lateinit var lvDirectionStops: ListView
    private lateinit var btnPickStop: Button
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
        } else {
            title = application.getString(R.string.label_create_direction)
        }
    }

    override fun onResume() {
        super.onResume()
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
    }

    private fun saveDirection() {
        val newDirection: Direction
        if (direction != null && etDirectionTitle.text.toString().trim() != "") {
            direction!!.routeStopIds = operations.convertListOfStopsToListOfStopIds(route)
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
        operations.saveDirection(applicationContext, lineId, direction, newDirection)
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
        }
        alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
        val alertDialog = alert.create()
        alertDialog.show()
    }
}