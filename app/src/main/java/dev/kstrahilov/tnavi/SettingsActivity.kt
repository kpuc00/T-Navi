package dev.kstrahilov.tnavi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.Gson


class SettingsActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var lvSettings: ListView
    private lateinit var settingsListAdapter: SettingsListAdapter
    private lateinit var settings: Array<String>
    private val operations = Operations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        title = application.getString(R.string.settings)

        lvSettings = findViewById(R.id.lv_settings)
        settings = arrayOf("stopManager", "lineManager", "exportData", "importData")
        settingsListAdapter = SettingsListAdapter(applicationContext, settings)

        lvSettings.adapter = settingsListAdapter
        lvSettings.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (settings[position]) {
            "stopManager" -> {
                val intent = Intent(this, StopManager::class.java)
                startActivity(intent)
            }
            "lineManager" -> {
//
            }
            "exportData" -> {
                exportData()
            }
            "importData" -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(application.getString(R.string.import_data))
                alert.setIcon(
                    AppCompatResources.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_file_upload_blue_24
                    )
                )
                alert.setMessage(application.getString(R.string.import_data_message))
                alert.setNeutralButton(application.getString(R.string.export_data)) { _, _ ->
                    exportData()
                    openFilePicker()
                }
                alert.setPositiveButton(application.getString(R.string.yes)) { _, _ ->
                    openFilePicker()
                }
                alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
                val alertDialog = alert.create()
                alertDialog.show()
            }
            else -> {
                Toast.makeText(
                    applicationContext,
                    application.getString(R.string.error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun exportData() {
        val stopsData = operations.loadStopsFromInternalStorage(applicationContext)
        val linesData = operations.loadLinesFromInternalStorage(applicationContext)
        val gson = Gson()

        operations.exportDataToExternalStorage(
            applicationContext, application, gson.toJson(
                mapOf("stopsData" to stopsData, "linesData" to linesData)
            )
        )
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            if (result.data != null) {
                val uri = result.data!!.data
                if (uri != null) {
                    if (operations.importFromExternalData(this, uri)) {
                        Toast.makeText(applicationContext, "done", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            return@registerForActivityResult
        }
    }


}