package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class StopManager : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var lvAllStops: ListView
    private lateinit var stops: ArrayList<Stop>
    private val operations = Operations()
    private lateinit var tvEmptyListStops: TextView
    private lateinit var etSearchStop: EditText
    private lateinit var adapter: ArrayAdapter<Stop>

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

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stops)
        lvAllStops.adapter = adapter
        lvAllStops.onItemClickListener = this
        operations.removeDeletedStopIdsFromRoutes(applicationContext)

        etSearchStop = findViewById(R.id.et_search_stop)
        etSearchStop.setText("")
        etSearchStop.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
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
        val intent = Intent(this, StopFormActivity::class.java)
        intent.putExtra("stop", adapter.getItem(position))
        startActivity(intent)
    }
}