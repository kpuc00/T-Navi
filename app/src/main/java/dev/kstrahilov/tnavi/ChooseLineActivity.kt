package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ChooseLineActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var lvLines: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_line)

        lvLines = findViewById(R.id.lv_lines)
        lvLines.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data())
        lvLines.onItemClickListener = this
    }


    private fun data(): ArrayList<Line> {
        val route0: ArrayList<Stop> = ArrayList()
        route0.add(Stop("Бл. 407 (Вл-во)", false, true, null, null))
        route0.add(Stop("кап. Петко войвода 2", false, false, null, null))
        route0.add(Stop("Детелина", false, false, null, null))
        route0.add(Stop("Вежен", false, false, null, null))
        route0.add(Stop("Мургаш", false, false, null, null))
        route0.add(Stop("Армейска", false, false, null, null))
        route0.add(Stop("ТИС Север", false, false, null, null))
        route0.add(Stop("Осъм", false, false, null, null))
        route0.add(Stop("3-ти март", false, false, null, null))
        route0.add(Stop("Искър", false, false, null, null))
        route0.add(Stop("Янтра", false, false, null, null))
        route0.add(Stop("Огоста", false, false, null, null))
        route0.add(Stop("Централна автобаза", false, false, null, null))
        route0.add(Stop("Дом Младост (за Аспарухово)", false, false, null, null))
        route0.add(Stop("8-ми септември", false, false, null, null))
        route0.add(Stop("Техникумите-Сливница", false, false, null, null))
        route0.add(Stop("Трансформатора", false, false, null, null))
        route0.add(Stop("Л.к. Тракия", false, false, null, null))
        route0.add(Stop("Нептун", false, false, null, null))
        route0.add(Stop("Централна поща (за Аспарухово)", false, false, null, null))
        route0.add(Stop("Полиграфия", false, false, null, null))
        route0.add(Stop("8-ми декември", false, false, null, null))
        route0.add(Stop("КЗ", false, false, null, null))
        route0.add(Stop("ИХА", false, false, null, null))
        route0.add(Stop("Лазур", false, false, null, null))
        route0.add(Stop("Калин", false, false, null, null))
        route0.add(Stop("Любен Каравелов", false, false, null, null))
        route0.add(Stop("Народни Будители", false, false, null, null))
        route0.add(Stop("Обръщач тролеи", false, false, null, null))

        val route1: ArrayList<Stop> = ArrayList()

        route1.add(Stop("Аспарухово център", false, true, null, null))
        route1.add(Stop("Народни Будители", false, false, null, null))
        route1.add(Stop("Любен Каравелов", false, false, null, null))
        route1.add(Stop("Калин", false, false, null, null))
        route1.add(Stop("Лазур", false, false, null, null))
        route1.add(Stop("КЗ", false, false, null, null))
        route1.add(Stop("8-ми декември", false, false, null, null))
        route1.add(Stop("Полиграфия", false, false, null, null))
        route1.add(Stop("Централна поща", false, false, null, null))
        route1.add(Stop("Нептун", false, false, null, null))
        route1.add(Stop("Л.к. Тракия", false, false, null, null))
        route1.add(Stop("Трансформатора", false, false, null, null))
        route1.add(Stop("Техникумите-Сливница", false, false, null, null))
        route1.add(Stop("8-ми септември", false, false, null, null))
        route1.add(Stop("Дом Младост /ул. Вяра за Вл-во/", false, false, null, null))
        route1.add(Stop("Централна автобаза", false, false, null, null))
        route1.add(Stop("Огоста", false, false, null, null))
        route1.add(Stop("Янтра", false, false, null, null))
        route1.add(Stop("Искър", false, false, null, null))
        route1.add(Stop("3-ти март", false, false, null, null))
        route1.add(Stop("Осъм", false, false, null, null))
        route1.add(Stop("ТИС Север", false, false, null, null))
        route1.add(Stop("Армейска", false, false, null, null))
        route1.add(Stop("Мургаш", false, false, null, null))
        route1.add(Stop("Вежен", false, false, null, null))
        route1.add(Stop("Детелина", false, false, null, null))
        route1.add(Stop("кап. Петко войвода 2", false, false, null, null))
        route1.add(Stop("Скалата", false, false, null, null))
        route1.add(Stop("Бл. 407 (Вл-во)", false, false, null, null))

        val directions: ArrayList<Direction> = ArrayList()
        directions.add(Direction("Аспарухово", route0))
        directions.add(Direction("Владиславово", route1))

        val lines: ArrayList<Line> = ArrayList()
        lines.add(Line("88", directions))

        val ro: ArrayList<Stop> = ArrayList()
        val directions2: ArrayList<Direction> = ArrayList()
        directions2.add(Direction("ЗА ДЕПО", ro))
        lines.add(Line("999", directions2))


        return lines

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val line: Line = data()[position]
        val intent = Intent(this, ChooseDirectionActivity::class.java)
        intent.putExtra("line", line)
        startActivity(intent)
    }
}