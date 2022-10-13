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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

class DirectionFormActivity : AppCompatActivity(), OnItemClickListener {
    private var lineId: String? = null
    private var direction: Direction? = null
    private lateinit var etDirectionTitle: EditText
    private lateinit var lvDirectionStops: ListView
    private lateinit var btnPickStop: Button
    private var route: ArrayList<Stop> = ArrayList()
    private var gson = Gson()

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

        if (direction != null) {
            title = application.getString(R.string.label_edit_direction)

            etDirectionTitle.setText(direction!!.title)
            etDirectionTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    direction!!.title = etDirectionTitle.text.toString()
                }

            })
        } else {
            title = application.getString(R.string.label_create_direction)
        }
    }

    override fun onResume() {
        super.onResume()
        lvDirectionStops = findViewById(R.id.lv_direction_stops)
        lvDirectionStops.adapter =
            direction?.let { ArrayAdapter(this, android.R.layout.simple_list_item_1, it.route) }
        lvDirectionStops.onItemClickListener = this
    }

    private fun saveDirection() {
        val newDirection: Direction
        if (direction != null && etDirectionTitle.text.toString() != "") {
            newDirection = direction!!
        } else {
            if (etDirectionTitle.text.toString() == "") {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(application.getString(R.string.error))
                alert.setMessage(application.getString(R.string.form_incomplete))
                alert.setNegativeButton(application.getString(R.string.ok)) { _, _ -> }
                val alertDialog = alert.create()
                alertDialog.show()
                return
            }
            newDirection = Direction(
                title = etDirectionTitle.text.toString().trim(), route = route
            )

        }

        val path: String = applicationContext.filesDir.toString()
        val fileName = "/lines.json"
        val file = File(path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val linesListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
                val readLines: ArrayList<Line> = gson.fromJson(readJson, linesListType)
                val currentLine: Line = readLines.filter { it.id.toString() == lineId }[0]

                //Updating the line with new a direction
                //First remove it and add it again when updated
                readLines.remove(currentLine)
                //Remove old direction to keep only the new one
                if (direction != null) {
                    val readDirection = currentLine.directions!!.filter {
                        it.id.toString() == direction!!.id.toString()
                    }[0]
                    currentLine.directions!!.remove(readDirection)
                }
                currentLine.directions!!.add(newDirection)
                currentLine.directions!!.sortBy { it.title.lowercase() }
                readLines.add(currentLine)
                readLines.sortBy { it.number }

                val jsonString: String = gson.toJson(readLines)
                file.writeText(jsonString, Charsets.UTF_8)
                checkIfDataWritten(file, jsonString, newDirection.title)
            } else {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    private fun checkIfDataWritten(
        file: File,
        jsonString: String,
        directionTitleToDisplay: String
    ) {
        if (file.readText(Charsets.UTF_8) == jsonString) {
            if (direction != null) {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.direction_)} $directionTitleToDisplay ${
                        applicationContext.getString(R.string.was_modified)
                    }",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.direction_)} $directionTitleToDisplay ${
                        applicationContext.getString(R.string.was_created)
                    }",
                    Toast.LENGTH_LONG
                ).show()
            }
            finish()
        } else {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteDirection() {
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/lines.json"
        val file = File(path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val linesListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
                val readLines: ArrayList<Line> = gson.fromJson(readJson, linesListType)
                val currentLine: Line = readLines.filter { it.id.toString() == lineId }[0]

                //Updating the line with removed direction
                //First remove it and add it again when updated
                readLines.remove(currentLine)
                //Remove direction
                if (direction != null) {
                    val readDirection = currentLine.directions!!.filter {
                        it.id.toString() == direction!!.id.toString()
                    }[0]
                    currentLine.directions!!.remove(readDirection)
                }
                readLines.add(currentLine)
                readLines.sortBy { it.number }

                val jsonString: String = gson.toJson(readLines)
                file.writeText(jsonString, Charsets.UTF_8)
                checkIfDataWritten(file, jsonString, direction!!.title)
            } else {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            finish()
        }
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
                deleteDirection()
            }
            alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
            val alertDialog = alert.create()
            alertDialog.show()
            finish()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        val stop: Stop = stops[position]
//        val intent = Intent(this, StopFormActivity::class.java)
//        intent.putExtra("stop", stop)
//        startActivity(intent)
    }
}