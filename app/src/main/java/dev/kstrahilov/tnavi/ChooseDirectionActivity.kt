package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ChooseDirectionActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var lvDirections: ListView
    private var line: Line? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_direction)
        val intent = intent
        line = intent.getSerializableExtra("line") as Line?
        title = application.getString(R.string.choose_direction) + " " + line.toString()

        lvDirections = findViewById(R.id.lv_directions)
        if (line != null) {
            lvDirections.adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, line!!.directions)
        }
        lvDirections.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val direction: Direction? = line?.directions?.get(position)
        val intent = Intent(this, RouteActivity::class.java)
        intent.putExtra("line", line.toString())
        intent.putExtra("lineAnnouncement", line?.announcementFilePath.toString())
        intent.putExtra("direction", direction)
        startActivity(intent)
    }
}