package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChooseDirectionActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var lvDirections: ListView
    private lateinit var line: Line
    private lateinit var tvEmptyListDestinations: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_direction)
        val intent = intent
        line = (intent.getParcelableExtra("line") as Line?)!!
        title = application.getString(R.string.choose_direction) + " " + line.toString()

        lvDirections = findViewById(R.id.lv_directions)
        tvEmptyListDestinations = findViewById(R.id.tv_empty_list_destinations)
        tvEmptyListDestinations.visibility = if (line.directions!!.size < 1) {
            View.VISIBLE
        } else View.GONE
        lvDirections.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, line.directions!!)
        lvDirections.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val direction: Direction = line.directions!![position]
        val intent = Intent(this, RouteActivity::class.java)
        intent.putExtra("line", line.toString())
        intent.putExtra("lineAnnouncement", line.announcementFilePath.toString())
        intent.putExtra("direction", direction)
        startActivity(intent)
    }
}