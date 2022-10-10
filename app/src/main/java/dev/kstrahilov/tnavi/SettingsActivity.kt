package dev.kstrahilov.tnavi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SettingsActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var lvSettings: ListView
    private lateinit var settingsListAdapter: SettingsListAdapter
    private lateinit var settings: Array<String>
    private lateinit var externalFile: File
    private lateinit var externalDirectory: File

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
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/stops.json"
        val stopsFile = File(path, fileName)
        var stopsData: String? = null
        if (stopsFile.exists()) {
            stopsData = stopsFile.readText(Charsets.UTF_8)
        }

        val externalPath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .toString()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val externalFileName = application.getString(R.string.app_name) +
                "/data_" + LocalDateTime.now().format(dateFormatter) + ".tnav"
        externalFile = File(externalPath, externalFileName)
        externalDirectory = File(application.getString(R.string.app_name))

        try {
            if (!externalDirectory.exists()) {
                externalDirectory.mkdir()
            }
            externalFile.parentFile?.mkdir()
            if (stopsData != null) {
                externalFile.writeText(stopsData, Charsets.UTF_8)
                Toast.makeText(
                    applicationContext,
                    application.getString(R.string.data_exported),
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    private fun importData(importedFilePath: String) {
        val importedFile = File(importedFilePath)
        val stopsData: String?

        val gson = Gson()
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/stops.json"
        val file = File(path, fileName)

        stopsData = importedFile.readText(Charsets.UTF_8)
        val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
        val readStops: ArrayList<Stop> = gson.fromJson(stopsData, stopListType)
        val jsonString: String = gson.toJson(readStops)
        file.writeText(jsonString, Charsets.UTF_8)
        Toast.makeText(
            applicationContext,
            applicationContext.getString(R.string.data_imported),
            Toast.LENGTH_LONG
        ).show()
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
                val uri: Uri? = result.data!!.data
                if (uri != null) {
                    Toast.makeText(
                        applicationContext,
                        uri.path.toString(),
                        Toast.LENGTH_LONG
                    ).show()
//                    importData(uri.path.toString())
                }
            } else {
                return@registerForActivityResult
            }
        }
    }

}