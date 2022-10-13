package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.text.InputType
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

class ChooseLineActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var lvLines: ListView
    private var isManager: Boolean = false
    private val operations = Operations()
    private lateinit var lines: ArrayList<Line>
    private lateinit var tvEmptyListLines: TextView
    private var line: Line? = null
    private var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_line)

        val intent = intent
        val manager = intent.getStringExtra("manager")
        if (manager != null && manager == "manager") {
            isManager = true
            title = application.getString(R.string.lines_manager)
        }
    }

    override fun onResume() {
        super.onResume()
        lines = operations.loadLinesFromInternalStorage(applicationContext)
        tvEmptyListLines = findViewById(R.id.tv_empty_list_lines)
        tvEmptyListLines.visibility = if (lines.size < 1) {
            View.VISIBLE
        } else View.GONE

        lvLines = findViewById(R.id.lv_lines)
        lvLines.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lines)
        lvLines.onItemClickListener = this
    }

    private fun saveLine(number: String) {
        val newLine: Line = if (line != null && number != "") {
            line!!
        } else {
            if (number == "") {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(application.getString(R.string.error))
                alert.setMessage(application.getString(R.string.form_incomplete))
                alert.setNegativeButton(application.getString(R.string.ok)) { _, _ -> }
                val alertDialog = alert.create()
                alertDialog.show()
                return
            }
            Line(number = number)
        }

        val path: String = applicationContext.filesDir.toString()
        val fileName = "/lines.json"
        val file = File(path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val linesListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
                val readLines: ArrayList<Line> = gson.fromJson(readJson, linesListType)
                if (line != null) {
                    Toast.makeText(applicationContext, "not null", Toast.LENGTH_SHORT).show()
                    val currentLine: Line =
                        readLines.filter { it.id.toString() == line?.id.toString() }[0]

                    //Updating the line
                    //First remove it and add it again when updated
                    readLines.remove(currentLine)
                    readLines.add(newLine)
                } else {
                    if (readLines.any { it.number == newLine.number }) {
                        val alert = AlertDialog.Builder(this)
                        alert.setTitle(application.getString(R.string.error))
                        alert.setMessage(application.getString(R.string.line_exists))
                        alert.setNegativeButton(application.getString(R.string.ok)) { _, _ -> }
                        val alertDialog = alert.create()
                        alertDialog.show()
                        return
                    } else {
                        readLines.add(newLine)
                    }
                }
                readLines.sortBy { it.number }
                val jsonString: String = gson.toJson(readLines)
                file.writeText(jsonString, Charsets.UTF_8)
                line?.let { checkIfDataWritten(file, jsonString, it.number) }

            } else {
                val jsonString: String = gson.toJson(arrayListOf(newLine))
                file.writeText(jsonString, Charsets.UTF_8)
                line?.let { checkIfDataWritten(file, jsonString, it.number) }
            }
            val intent = Intent(this, DirectionFormActivity::class.java)
            intent.putExtra("lineId", newLine.id.toString())
            startActivity(intent)
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
        file: File, jsonString: String, directionTitleToDisplay: String
    ) {
        if (file.readText(Charsets.UTF_8) == jsonString) {
            if (line != null) {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.line_)} $directionTitleToDisplay ${
                        applicationContext.getString(R.string.was_modified)
                    }",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.line_)} $directionTitleToDisplay ${
                        applicationContext.getString(R.string.was_created)
                    }",
                    Toast.LENGTH_SHORT
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (isManager) {
            menuInflater.inflate(R.menu.menu_add, menu)
            true
        } else false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(application.getString(R.string.label_create_line))
            val input = EditText(this)
            input.hint = application.getString(R.string.line_number)
            input.inputType = InputType.TYPE_CLASS_TEXT
            alert.setView(input)
            alert.setPositiveButton(application.getString(R.string.action_create)) { _, _ ->
                saveLine(input.text.toString().trim())
            }
            alert.setNegativeButton(application.getString(R.string.cancel)) { _, _ -> }
            val alertDialog = alert.create()
            alertDialog.show()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val line: Line = lines[position]
        val intent = Intent(this, ChooseDirectionActivity::class.java)
        intent.putExtra("line", line)
        if (isManager) {
            intent.putExtra("manager", "manager")
        }
        startActivity(intent)

    }
}