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
        val lines: ArrayList<Line> = ArrayList()

        val route821: ArrayList<Stop> = ArrayList()
        route821.add(Stop("Бл. 407 (Вл-во)", false, true, null, null))
        route821.add(Stop("кап. Петко войвода 2", false, false, null, null))
        route821.add(Stop("Детелина", false, false, null, null))
        route821.add(Stop("Вежен", false, false, null, null))
        route821.add(Stop("Мургаш", false, false, null, null))
        route821.add(Stop("Армейска", false, false, null, null))
        route821.add(Stop("ТИС Север", false, false, null, null))
        route821.add(Stop("KAT", false, false, null, null))
        route821.add(Stop("Парк-музей Вл. Варненчик", false, false, null, null))
        route821.add(Stop("Мебелна палата", false, false, null, null))
        route821.add(Stop("Веселин Ханчев", false, false, null, null))
        route821.add(Stop("Петя Дубарова", false, false, null, null))
        route821.add(Stop("Георги Петлеешев", false, false, null, null))
        route821.add(Stop("Дом Младост", false, false, null, null))
        route821.add(Stop("8-ми септември", false, false, null, null))
        route821.add(Stop("Техникумите-Сливница", false, false, null, null))
        route821.add(Stop("Трансформатора", false, false, null, null))
        route821.add(Stop("Л.к. Тракия", false, false, null, null))
        route821.add(Stop("Нептун", false, false, null, null))
        route821.add(Stop("Централна поща /за ЖПГ/", false, false, null, null))
        route821.add(Stop("Димчо Дебелянов", false, false, null, null))
        route821.add(Stop("ЖП Гара /пл. Славейков за КК/", false, false, null, null))

        val route822: ArrayList<Stop> = ArrayList()
        route822.add(Stop("ЖП Гара /пл. Славейков/", false, true, null, null))
        route822.add(Stop("Димчо Дебелянов", false, false, null, null))
        route822.add(Stop("Централна поща", false, false, null, null))
        route822.add(Stop("Нептун", false, false, null, null))
        route822.add(Stop("Л.к. Тракия", false, false, null, null))
        route822.add(Stop("Трансформатора", false, false, null, null))
        route822.add(Stop("Техникумите-Сливница", false, false, null, null))
        route822.add(Stop("8-ми септември", false, false, null, null))
        route822.add(Stop("Дом Младост /Вл-во/", false, false, null, null))
        route822.add(Stop("Георги Петлешев", false, false, null, null))
        route822.add(Stop("Петя Дубарова", false, false, null, null))
        route822.add(Stop("Веселин Ханчев", false, false, null, null))
        route822.add(Stop("Мебелна палата", false, false, null, null))
        route822.add(Stop("Парк-музей Вл. Варненчик", false, false, null, null))
        route822.add(Stop("КАТ", false, false, null, null))
        route822.add(Stop("ТИС Север /до ТЦ за Вл-во/", false, false, null, null))
        route822.add(Stop("Армейска", false, false, null, null))
        route822.add(Stop("Мургаш", false, false, null, null))
        route822.add(Stop("Вежен", false, false, null, null))
        route822.add(Stop("Детелина", false, false, null, null))
        route822.add(Stop("кап. Петко войвода 2", false, false, null, null))
        route822.add(Stop("Скалата", false, false, null, null))
        route822.add(Stop("Бл. 407 (Вл-во)", false, false, null, null))

        val directions82: ArrayList<Direction> = ArrayList()
        directions82.add(Direction("ЖП Гара", route821))
        directions82.add(Direction("Владиславово", route822))
        lines.add(Line("82", directions82))

        val route831: ArrayList<Stop> = ArrayList()
        route831.add(Stop("ТИС Север", false, true, null, null))
        route831.add(Stop("Осъм", false, false, null, null))
        route831.add(Stop("3-ти март", false, false, null, null))
        route831.add(Stop("Искър", false, false, null, null))
        route831.add(Stop("Янтра", false, false, null, null))
        route831.add(Stop("Огоста", false, false, null, null))
        route831.add(Stop("Централна автобаза", false, false, null, null))
        route831.add(Stop("Дом Младост", false, false, null, null))
        route831.add(Stop("8-ми септември", false, false, null, null))
        route831.add(Stop("Техникумите-Сливница", false, false, null, null))
        route831.add(Stop("Трансформатора", false, false, null, null))
        route831.add(Stop("Л.к. Тракия", false, false, null, null))
        route831.add(Stop("Нептун", false, false, null, null))
        route831.add(Stop("Централна поща /за ЖПГ/", false, false, null, null))
        route831.add(Stop("Димчо Дебелянов", false, false, null, null))
        route831.add(Stop("ЖП Гара /пл. Славейков за КК/", false, false, null, null))

        val route832: ArrayList<Stop> = ArrayList()
        route832.add(Stop("ЖП Гара /пл. Славейков/", false, true, null, null))
        route832.add(Stop("Димчо Дебелянов", false, false, null, null))
        route832.add(Stop("Централна поща", false, false, null, null))
        route832.add(Stop("Нептун", false, false, null, null))
        route832.add(Stop("Л.к. Тракия", false, false, null, null))
        route832.add(Stop("Трансформатора", false, false, null, null))
        route832.add(Stop("Техникумите-Сливница", false, false, null, null))
        route832.add(Stop("8-ми септември", false, false, null, null))
        route832.add(Stop("Дом Младост /ул. Вяра/", false, false, null, null))
        route832.add(Stop("Централна автобаза", false, false, null, null))
        route832.add(Stop("Огоста", false, false, null, null))
        route832.add(Stop("Янтра", false, false, null, null))
        route832.add(Stop("Искър", false, false, null, null))
        route832.add(Stop("3-ти март", false, false, null, null))
        route832.add(Stop("Осъм", false, false, null, null))
        route832.add(Stop("ТИС Север", false, false, null, null))

        val directions83: ArrayList<Direction> = ArrayList()
        directions83.add(Direction("ЖП Гара", route831))
        directions83.add(Direction("ТИС Север", route832))
        lines.add(Line("83", directions83))

        val route861: ArrayList<Stop> = ArrayList()
        route861.add(Stop("Почивка /Обръщач/", false, true, null, null))
        route861.add(Stop("Почивка-2", false, false, null, null))
        route861.add(Stop("Ученически к-с", false, false, null, null))
        route861.add(Stop("Средношколска", false, false, null, null))
        route861.add(Stop("Стадион Варна", false, false, null, null))
        route861.add(Stop("Спортист", false, false, null, null))
        route861.add(Stop("Явор", false, false, null, null))
        route861.add(Stop("Паметника", false, false, null, null))
        route861.add(Stop("Чаталджа", false, false, null, null))
        route861.add(Stop("пл. Съединение", false, false, null, null))
        route861.add(Stop("Музея", false, false, null, null))
        route861.add(Stop("Централна Поща", false, false, null, null))
        route861.add(Stop("Полиграфия", false, false, null, null))
        route861.add(Stop("8-ми декември", false, false, null, null))
        route861.add(Stop("КЗ", false, false, null, null))
        route861.add(Stop("ИХА", false, false, null, null))
        route861.add(Stop("Лазур", false, false, null, null))
        route861.add(Stop("Калин", false, false, null, null))
        route861.add(Stop("Любен Каравелов", false, false, null, null))
        route861.add(Stop("Народни Будители", false, false, null, null))
        route861.add(Stop("Обръщач тролеи", false, false, null, null))

        val route862: ArrayList<Stop> = ArrayList()
        route862.add(Stop("Обръщач тролеи", false, true, null, null))
        route862.add(Stop("Център", false, false, null, null))
        route862.add(Stop("Народни Будители", false, false, null, null))
        route862.add(Stop("Любен Каравелов", false, false, null, null))
        route862.add(Stop("Калин", false, false, null, null))
        route862.add(Stop("Лазур", false, false, null, null))
        route862.add(Stop("КЗ", false, false, null, null))
        route862.add(Stop("8-ми декември", false, false, null, null))
        route862.add(Stop("Полиграфия", false, false, null, null))
        route862.add(Stop("Централна поща", false, false, null, null))
        route862.add(Stop("Музея", false, false, null, null))
        route862.add(Stop("пл. Съединение", false, false, null, null))
        route862.add(Stop("Чаталджа", false, false, null, null))
        route862.add(Stop("Паметника", false, false, null, null))
        route862.add(Stop("Явор", false, false, null, null))
        route862.add(Stop("Спортист", false, false, null, null))
        route862.add(Stop("Стадион Варна", false, false, null, null))
        route862.add(Stop("Средношколска", false, false, null, null))
        route862.add(Stop("Карин дом", false, false, null, null))
        route862.add(Stop("Ученически к-с", false, false, null, null))
        route862.add(Stop("Почивка /Обръщач/", false, false, null, null))

        val directions86: ArrayList<Direction> = ArrayList()
        directions86.add(Direction("Аспарухово", route861))
        directions86.add(Direction("Почивка", route862))
        lines.add(Line("86", directions86))

        val route881: ArrayList<Stop> = ArrayList()
        route881.add(Stop("Бл. 407 (Вл-во)", false, true, null, null))
        route881.add(Stop("кап. Петко войвода 2", false, false, null, null))
        route881.add(Stop("Детелина", false, false, null, null))
        route881.add(Stop("Вежен", false, false, null, null))
        route881.add(Stop("Мургаш", false, false, null, null))
        route881.add(Stop("Армейска", false, false, null, null))
        route881.add(Stop("ТИС Север", false, false, null, null))
        route881.add(Stop("Осъм", false, false, null, null))
        route881.add(Stop("3-ти март", false, false, null, null))
        route881.add(Stop("Искър", false, false, null, null))
        route881.add(Stop("Янтра", false, false, null, null))
        route881.add(Stop("Огоста", false, false, null, null))
        route881.add(Stop("Централна автобаза", false, false, null, null))
        route881.add(Stop("Дом Младост (за Аспарухово)", false, false, null, null))
        route881.add(Stop("8-ми септември", false, false, null, null))
        route881.add(Stop("Техникумите-Сливница", false, false, null, null))
        route881.add(Stop("Трансформатора", false, false, null, null))
        route881.add(Stop("Л.к. Тракия", false, false, null, null))
        route881.add(Stop("Нептун", false, false, null, null))
        route881.add(Stop("Централна поща (за Аспарухово)", false, false, null, null))
        route881.add(Stop("Полиграфия", false, false, null, null))
        route881.add(Stop("8-ми декември", false, false, null, null))
        route881.add(Stop("КЗ", false, false, null, null))
        route881.add(Stop("ИХА", false, false, null, null))
        route881.add(Stop("Лазур", false, false, null, null))
        route881.add(Stop("Калин", false, false, null, null))
        route881.add(Stop("Любен Каравелов", false, false, null, null))
        route881.add(Stop("Народни Будители", false, false, null, null))
        route881.add(Stop("Обръщач тролеи", false, false, null, null))

        val route882: ArrayList<Stop> = ArrayList()
        route882.add(Stop("Аспарухово център", false, true, null, null))
        route882.add(Stop("Народни Будители", false, false, null, null))
        route882.add(Stop("Любен Каравелов", false, false, null, null))
        route882.add(Stop("Калин", false, false, null, null))
        route882.add(Stop("Лазур", false, false, null, null))
        route882.add(Stop("КЗ", false, false, null, null))
        route882.add(Stop("8-ми декември", false, false, null, null))
        route882.add(Stop("Полиграфия", false, false, null, null))
        route882.add(Stop("Централна поща", false, false, null, null))
        route882.add(Stop("Нептун", false, false, null, null))
        route882.add(Stop("Л.к. Тракия", false, false, null, null))
        route882.add(Stop("Трансформатора", false, false, null, null))
        route882.add(Stop("Техникумите-Сливница", false, false, null, null))
        route882.add(Stop("8-ми септември", false, false, null, null))
        route882.add(Stop("Дом Младост /ул. Вяра за Вл-во/", false, false, null, null))
        route882.add(Stop("Централна автобаза", false, false, null, null))
        route882.add(Stop("Огоста", false, false, null, null))
        route882.add(Stop("Янтра", false, false, null, null))
        route882.add(Stop("Искър", false, false, null, null))
        route882.add(Stop("3-ти март", false, false, null, null))
        route882.add(Stop("Осъм", false, false, null, null))
        route882.add(Stop("ТИС Север", false, false, null, null))
        route882.add(Stop("Армейска", false, false, null, null))
        route882.add(Stop("Мургаш", false, false, null, null))
        route882.add(Stop("Вежен", false, false, null, null))
        route882.add(Stop("Детелина", false, false, null, null))
        route882.add(Stop("кап. Петко войвода 2", false, false, null, null))
        route882.add(Stop("Скалата", false, false, null, null))
        route882.add(Stop("Бл. 407 (Вл-во)", false, false, null, null))

        val directions88: ArrayList<Direction> = ArrayList()
        directions88.add(Direction("Аспарухово", route881))
        directions88.add(Direction("Владиславово", route882))
        lines.add(Line("88", directions88))

        val empty: ArrayList<Stop> = ArrayList()
        val directions999: ArrayList<Direction> = ArrayList()
        directions999.add(Direction("ЗА ДЕПО", empty))
        directions999.add(Direction("Служебен", empty))
        directions999.add(Direction("ТЕСТ ДРАЙВ", empty))
        directions999.add(Direction("УЧИЛИЩЕН", empty))
        lines.add(Line("999", directions999))

        return lines
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val line: Line = data()[position]
        val intent = Intent(this, ChooseDirectionActivity::class.java)
        intent.putExtra("line", line)
        startActivity(intent)
    }
}