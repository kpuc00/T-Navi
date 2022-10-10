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
import com.google.android.gms.maps.model.LatLng

class StopManager : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var lvAllStops: ListView
    private lateinit var stops: ArrayList<Stop>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_manager)
        title = application.getString(R.string.stops_manager)

        lvAllStops = findViewById(R.id.lv_all_stops)

        stops = ArrayList()
        stops.add(Stop(title = "Бл. 407 (Вл-во)"))
        stops.add(Stop(title = "кап. Петко войвода 2"))
        stops.add(Stop(title = "Детелина"))
        stops.add(Stop(title = "Вежен"))
        stops.add(Stop(title = "Мургаш"))
        stops.add(Stop(title = "Армейска"))
        stops.add(Stop(title = "ТИС Север"))
        stops.add(Stop(title = "Осъм"))
        stops.add(Stop(title = "3-ти март"))
        stops.add(Stop(title = "Искър"))
        stops.add(Stop(title = "Янтра"))
        stops.add(Stop(title = "Огоста"))
        stops.add(Stop(title = "Централна автобаза"))
        stops.add(Stop(title = "Дом Младост (за Аспарухово)"))
        stops.add(Stop(title = "8-ми септември"))
        stops.add(Stop(title = "Техникумите-Сливница"))
        stops.add(Stop(title = "Трансформатора"))
        stops.add(Stop(title = "Л.к. Тракия"))
        stops.add(Stop(title = "Нептун"))
        stops.add(
            Stop(
                title = "Централна поща (за Аспарухово)", location = LatLng(51.4652, 5.452)
            )
        )
        stops.add(Stop(title = "Полиграфия"))
        stops.add(Stop(title = "8-ми декември"))
        stops.add(Stop(title = "КЗ"))
        stops.add(Stop(title = "ИХА"))
        stops.add(Stop(title = "Лазур"))
        stops.add(Stop(title = "Калин"))
        stops.add(Stop(title = "Любен Каравелов"))
        stops.add(Stop(title = "Народни Будители"))
        stops.add(Stop(title = "Обръщач тролеи"))

        lvAllStops.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stops)
        lvAllStops.onItemClickListener = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            val intent = Intent(this, CreateStop::class.java)
            startActivity(intent)

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val stop: Stop = stops[position]
        val intent = Intent(this, CreateStop::class.java)
        intent.putExtra("stop", stop)
        startActivity(intent)
    }
}