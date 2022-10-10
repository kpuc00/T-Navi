package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var lvSettings: ListView
    private lateinit var settingsListAdapter: SettingsListAdapter
    private lateinit var settings: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        title = application.getString(R.string.settings)

        lvSettings = findViewById(R.id.lv_settings)
        settings = arrayOf("stopManager", "lineManager")
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
}