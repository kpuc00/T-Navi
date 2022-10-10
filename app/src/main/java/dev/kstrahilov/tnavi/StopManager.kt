package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

class StopManager : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var lvAllStops: ListView
    private lateinit var stops: ArrayList<Stop>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_manager)
        title = application.getString(R.string.stops_manager)

        lvAllStops = findViewById(R.id.lv_all_stops)

        loadStops()
    }

    override fun onResume() {
        super.onResume()
        loadStops()
    }

    private fun loadStops() {
        val gson = Gson()
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/stops.json"
        val file = File(path, fileName)

        stops = if (file.exists()) {
            val readJson = file.readText(Charsets.UTF_8)
            val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
            gson.fromJson(readJson, stopListType)
        } else {
            ArrayList()
        }

        lvAllStops.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stops)
        lvAllStops.onItemClickListener = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            val intent = Intent(this, StopFormActivity::class.java)
            startActivity(intent)

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val stop: Stop = stops[position]
        val intent = Intent(this, StopFormActivity::class.java)
        intent.putExtra("stop", stop)
        startActivity(intent)
    }
}