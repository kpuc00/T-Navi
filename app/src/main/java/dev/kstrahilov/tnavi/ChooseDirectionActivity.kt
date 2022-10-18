package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class ChooseDirectionActivity : AppCompatActivity(), OnItemClickListener {
    private var isManager: Boolean = false
    private lateinit var lvDirections: ListView
    private lateinit var line: Line
    private lateinit var tvEmptyListDestinations: TextView
    private val operations = Operations()
    private lateinit var storedLine: Line

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_direction)
        val intent = intent
        line = (intent.getParcelableExtra("line") as Line?)!!

        val manager = intent.getStringExtra("manager")
        if (manager != null && manager == "manager") {
            isManager = true
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            storedLine =
                operations.loadLinesFromInternalStorage(applicationContext)
                    .find { it.id == line.id }!!
        } catch (_: java.lang.NullPointerException) {
        }
        title = application.getString(R.string.choose_direction) + " " + storedLine.toString()
        lvDirections = findViewById(R.id.lv_directions)
        tvEmptyListDestinations = findViewById(R.id.tv_empty_list_destinations)
        tvEmptyListDestinations.visibility = if (storedLine.directions!!.size < 1) {
            View.VISIBLE
        } else View.GONE
        lvDirections.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, storedLine.directions!!)
        lvDirections.onItemClickListener = this
        lvDirections.invalidateViews()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val direction: Direction = storedLine.directions!![position]
        if (isManager) {
            val intent = Intent(this, DirectionFormActivity::class.java)
            intent.putExtra("lineId", storedLine.id.toString())
            intent.putExtra("direction", direction)
            startActivity(intent)
        } else {
            val intent = Intent(this, RouteActivity::class.java)
            intent.putExtra("line", storedLine.toString())
            intent.putExtra("lineAnnouncement", storedLine.announcementFileName)
            intent.putExtra("direction", direction)
            startActivity(intent)
        }
    }

    private fun removeLine() {
        val storedLines = operations.loadLinesFromInternalStorage(applicationContext)
            .filter { it.id != storedLine.id }

        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/storage")
        directory.mkdir()
        val fileName = "/lines.json"
        val file = File(directory.path, fileName)

        val jsonString: String = operations.gson.toJson(storedLines)
        file.writeText(jsonString, Charsets.UTF_8)
        Toast.makeText(
            applicationContext,
            "${applicationContext.getString(R.string.line_)} ${storedLine.number} ${
                applicationContext.getString(
                    R.string.was_deleted
                )
            }",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isManager) {
            menuInflater.inflate(R.menu.menu_edit, menu)
            menuInflater.inflate(R.menu.menu_delete, menu)
            menuInflater.inflate(R.menu.menu_add, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_edit -> {
            val intent = Intent(this, EditLineActivity::class.java)
            intent.putExtra("line", storedLine)
            startActivity(intent)
            true
        }
        R.id.action_delete -> {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(application.getString(R.string.action_delete))
            alert.setMessage(application.getString(R.string.line_delete))
            alert.setPositiveButton(application.getString(R.string.yes)) { _, _ ->
                removeLine()
                finish()
            }
            alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
            val alertDialog = alert.create()
            alertDialog.show()

            true
        }
        R.id.action_add -> {
            val intent = Intent(this, DirectionFormActivity::class.java)
            intent.putExtra("lineId", storedLine.id.toString())
            startActivity(intent)

            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}