package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StopManager : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var lvAllStops: ListView
    private lateinit var stops: ArrayList<Stop>
    private val operations = Operations()
    private lateinit var tvEmptyListStops: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_manager)
        title = application.getString(R.string.stops_manager)

        lvAllStops = findViewById(R.id.lv_all_stops)
    }

    override fun onResume() {
        super.onResume()
        stops = operations.loadStopsFromInternalStorage(applicationContext)
        tvEmptyListStops = findViewById(R.id.tv_empty_list_stops)
        tvEmptyListStops.visibility = if (stops.size < 1) {
            View.VISIBLE
        } else View.GONE
        lvAllStops.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stops)
        lvAllStops.onItemClickListener = this
        operations.removeDeletedStopIdsFromRoutes(applicationContext)
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