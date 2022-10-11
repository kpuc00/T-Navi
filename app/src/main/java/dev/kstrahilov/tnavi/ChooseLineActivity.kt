package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng

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
        route821.add(Stop(title = "Бл. 407 (Вл-во)"))
        route821.add(Stop(title = "кап. Петко войвода 2"))
        route821.add(Stop(title = "Детелина"))
        route821.add(Stop(title = "Вежен"))
        route821.add(Stop(title = "Мургаш"))
        route821.add(Stop(title = "Армейска"))
        route821.add(Stop(title = "ТИС Север /до ТЦ за ЖПГ/"))
        route821.add(Stop(title = "KAT"))
        route821.add(Stop(title = "Парк-музей Вл. Варненчик"))
        route821.add(Stop(title = "Мебелна палата"))
        route821.add(Stop(title = "В. Ханчев"))
        route821.add(Stop(title = "Петя Дубарова"))
        route821.add(Stop(title = "Георги Петлеешев"))
        route821.add(Stop(title = "Дом Младост"))
        route821.add(Stop(title = "8-ми септември"))
        route821.add(Stop(title = "Техникумите-Сливница"))
        route821.add(Stop(title = "Трансформатора"))
        route821.add(Stop(title = "Л.к. Тракия"))
        route821.add(Stop(title = "Нептун"))
        route821.add(Stop(title = "Централна поща /за ЖПГ/"))
        route821.add(Stop(title = "Димчо Дебелянов"))
        route821.add(Stop(title = "ЖПГ /пл. Славейков за КК/"))

        val route822: ArrayList<Stop> = ArrayList()
        route822.add(Stop(title = "ЖПГ /пл. Славейков за център/"))
        route822.add(Stop(title = "Димчо Дебелянов"))
        route822.add(Stop(title = "Централна поща"))
        route822.add(Stop(title = "Нептун"))
        route822.add(Stop(title = "Л.к. Тракия"))
        route822.add(Stop(title = "Трансформатора"))
        route822.add(Stop(title = "Техникумите-Сливница"))
        route822.add(Stop(title = "8-ми септември"))
        route822.add(Stop(title = "Дом Младост за /Вл-во/"))
        route822.add(Stop(title = "Георги Петлешев"))
        route822.add(Stop(title = "Петя Дубарова"))
        route822.add(Stop(title = "В. Ханчев"))
        route822.add(Stop(title = "Мебелна палата"))
        route822.add(Stop(title = "Парк-музей Вл. Варненчик"))
        route822.add(Stop(title = "КАТ"))
        route822.add(Stop(title = "ТИС Север /до ТЦ за Вл-во/"))
        route822.add(Stop(title = "Армейска"))
        route822.add(Stop(title = "Мургаш"))
        route822.add(Stop(title = "Вежен"))
        route822.add(Stop(title = "Детелина"))
        route822.add(Stop(title = "кап. Петко войвода 2"))
        route822.add(Stop(title = "Скалата"))
        route822.add(Stop(title = "Бл. 407 (Вл-во)"))

        val route823: ArrayList<Stop> = ArrayList()
        route823.add(Stop(title = "ЖПГ /пл. Славейков за център/"))
        route823.add(Stop(title = "Димчо Дебелянов"))
        route823.add(Stop(title = "Централна поща"))
        route823.add(Stop(title = "Нептун"))
        route823.add(Stop(title = "Л.к. Тракия"))
        route823.add(Stop(title = "Трансформатора"))
        route823.add(Stop(title = "Техникумите-Сливница"))
        route823.add(Stop(title = "8-ми септември"))
        route823.add(Stop(title = "Дом Младост за /Вл-во/"))
        route823.add(Stop(title = "Георги Петлешев"))
        route823.add(Stop(title = "Петя Дубарова"))
        route823.add(Stop(title = "В. Ханчев"))
        route823.add(Stop(title = "Мебелна палата"))

        val route824: ArrayList<Stop> = ArrayList()
        route824.add(Stop(title = "ул. Brunelleschiweg", location = LatLng(51.465377, 5.452131)))
        route824.add(Stop(title = "Площадката", location = LatLng(51.464461, 5.453508)))
        route824.add(Stop(title = "Светофара", location = LatLng(51.462158, 5.454358)))
        route824.add(Stop(title = "Шел", location = LatLng(51.457632, 5.449686)))
        route824.add(Stop(title = "Парк Филипс", location = LatLng(51.456033, 5.448543)))
        route824.add(Stop(title = "Strijp-T", location = LatLng(51.452762, 5.453117)))
        route824.add(Stop(title = "Фонтис /Strijp-T/", location = LatLng(51.451257, 5.453188)))

        val route825: ArrayList<Stop> = ArrayList()
        route825.add(Stop(title = "Фонтис /Strijp-T/", location = LatLng(51.451257, 5.453188)))
        route825.add(Stop(title = "Strijp-T", location = LatLng(51.452762, 5.453117)))
        route825.add(Stop(title = "Парк Филипс", location = LatLng(51.456033, 5.448543)))
        route825.add(Stop(title = "Шел", location = LatLng(51.457632, 5.449686)))
        route825.add(Stop(title = "Светофара", location = LatLng(51.462158, 5.454358)))
        route825.add(Stop(title = "Площадката", location = LatLng(51.464461, 5.453508)))
        route825.add(Stop(title = "ул. Brunelleschiweg", location = LatLng(51.465377, 5.452131)))

        val directions82: ArrayList<Direction> = ArrayList()
        directions82.add(Direction(title = "ЖП Гара", route821, R.raw.jp_gara))
        directions82.add(Direction(title = "Владиславово", route822, R.raw.vladislavovo))
        directions82.add(Direction(title = "Мебелна палата", route823))
        directions82.add(Direction(title = "Училищен", route824))
        directions82.add(Direction(title = "ЗА ДЕПО", route825))
        lines.add(Line(number = "82", directions82, announcementFilePath = R.raw._82))

        val route831: ArrayList<Stop> = ArrayList()
        route831.add(Stop(title = "ТИС Север"))
        route831.add(Stop(title = "Осъм"))
        route831.add(Stop(title = "3-ти март"))
        route831.add(Stop(title = "Искър"))
        route831.add(Stop(title = "Янтра"))
        route831.add(Stop(title = "Огоста"))
        route831.add(Stop(title = "Централна автобаза"))
        route831.add(Stop(title = "Дом Младост"))
        route831.add(Stop(title = "8-ми септември"))
        route831.add(Stop(title = "Техникумите-Сливница"))
        route831.add(Stop(title = "Трансформатора"))
        route831.add(Stop(title = "Л.к. Тракия"))
        route831.add(Stop(title = "Нептун"))
        route831.add(Stop(title = "Централна поща /за ЖПГ/"))
        route831.add(Stop(title = "Димчо Дебелянов"))
        route831.add(Stop(title = "ЖПГ /пл. Славейков за КК/"))

        val route832: ArrayList<Stop> = ArrayList()
        route832.add(Stop(title = "ЖПГ /пл. Славейков/"))
        route832.add(Stop(title = "Димчо Дебелянов"))
        route832.add(Stop(title = "Централна поща"))
        route832.add(Stop(title = "Нептун"))
        route832.add(Stop(title = "Л.к. Тракия"))
        route832.add(Stop(title = "Трансформатора"))
        route832.add(Stop(title = "Техникумите-Сливница"))
        route832.add(Stop(title = "8-ми септември"))
        route832.add(Stop(title = "Дом Младост /ул. Вяра/"))
        route832.add(Stop(title = "Централна автобаза"))
        route832.add(Stop(title = "Огоста"))
        route832.add(Stop(title = "Янтра"))
        route832.add(Stop(title = "Искър"))
        route832.add(Stop(title = "3-ти март"))
        route832.add(Stop(title = "Осъм"))
        route832.add(Stop(title = "ТИС Север"))

        val directions83: ArrayList<Direction> = ArrayList()
        directions83.add(Direction(title = "ЖП Гара", route831, R.raw.jp_gara))
        directions83.add(Direction(title = "ТИС Север", route832, R.raw.tis_sever))
        lines.add(Line(number = "83", directions83, R.raw._83))

        val route861: ArrayList<Stop> = ArrayList()
        route861.add(Stop(title = "Почивка /Обръщач/"))
        route861.add(Stop(title = "Почивка-2"))
        route861.add(Stop(title = "Ученически к-с"))
        route861.add(Stop(title = "Средношколска"))
        route861.add(Stop(title = "Стадион Варна"))
        route861.add(Stop(title = "Спортист"))
        route861.add(Stop(title = "Явор"))
        route861.add(Stop(title = "Паметника"))
        route861.add(Stop(title = "Чаталджа"))
        route861.add(Stop(title = "пл. Съединение"))
        route861.add(Stop(title = "Музея"))
        route861.add(Stop(title = "Централна Поща"))
        route861.add(Stop(title = "Полиграфия"))
        route861.add(Stop(title = "8-ми декември"))
        route861.add(Stop(title = "КЗ"))
        route861.add(Stop(title = "ИХА"))
        route861.add(Stop(title = "Лазур"))
        route861.add(Stop(title = "Калин"))
        route861.add(Stop(title = "Любен Каравелов"))
        route861.add(Stop(title = "Народни Будители"))
        route861.add(Stop(title = "Обръщач тролеи"))

        val route862: ArrayList<Stop> = ArrayList()
        route862.add(Stop(title = "Обръщач тролеи"))
        route862.add(Stop(title = "Център"))
        route862.add(Stop(title = "Народни Будители"))
        route862.add(Stop(title = "Любен Каравелов"))
        route862.add(Stop(title = "Калин"))
        route862.add(Stop(title = "Лазур"))
        route862.add(Stop(title = "КЗ"))
        route862.add(Stop(title = "8-ми декември"))
        route862.add(Stop(title = "Полиграфия"))
        route862.add(Stop(title = "Централна поща"))
        route862.add(Stop(title = "Музея"))
        route862.add(Stop(title = "пл. Съединение"))
        route862.add(Stop(title = "Чаталджа"))
        route862.add(Stop(title = "Паметника"))
        route862.add(Stop(title = "Явор"))
        route862.add(Stop(title = "Спортист"))
        route862.add(Stop(title = "Стадион Варна"))
        route862.add(Stop(title = "Средношколска"))
        route862.add(Stop(title = "Карин дом"))
        route862.add(Stop(title = "Ученически к-с"))
        route862.add(Stop(title = "Почивка /Обръщач/"))

        val directions86: ArrayList<Direction> = ArrayList()
        directions86.add(Direction(title = "Аспарухово", route861, R.raw.asparuhovo))
        directions86.add(Direction(title = "Почивка", route862))
        lines.add(Line(number = "86", directions86))

        val route881: ArrayList<Stop> = ArrayList()
        route881.add(Stop(title = "Бл. 407 (Вл-во)"))
        route881.add(Stop(title = "кап. Петко войвода 2"))
        route881.add(Stop(title = "Детелина"))
        route881.add(Stop(title = "Вежен"))
        route881.add(Stop(title = "Мургаш"))
        route881.add(Stop(title = "Армейска"))
        route881.add(Stop(title = "ТИС Север"))
        route881.add(Stop(title = "Осъм"))
        route881.add(Stop(title = "3-ти март"))
        route881.add(Stop(title = "Искър"))
        route881.add(Stop(title = "Янтра"))
        route881.add(Stop(title = "Огоста"))
        route881.add(Stop(title = "Централна автобаза"))
        route881.add(Stop(title = "Дом Младост (за Аспарухово)"))
        route881.add(Stop(title = "8-ми септември"))
        route881.add(Stop(title = "Техникумите-Сливница"))
        route881.add(Stop(title = "Трансформатора"))
        route881.add(Stop(title = "Л.к. Тракия"))
        route881.add(Stop(title = "Нептун"))
        route881.add(
            Stop(
                title = "Централна поща (за Аспарухово)", location = LatLng(51.4652, 5.452)
            )
        )
        route881.add(Stop(title = "Полиграфия"))
        route881.add(Stop(title = "8-ми декември"))
        route881.add(Stop(title = "КЗ"))
        route881.add(Stop(title = "ИХА"))
        route881.add(Stop(title = "Лазур"))
        route881.add(Stop(title = "Калин"))
        route881.add(Stop(title = "Любен Каравелов"))
        route881.add(Stop(title = "Народни Будители"))
        route881.add(Stop(title = "Обръщач тролеи"))

        val route882: ArrayList<Stop> = ArrayList()
        route882.add(Stop(title = "Аспарухово център"))
        route882.add(Stop(title = "Народни Будители"))
        route882.add(Stop(title = "Любен Каравелов", location = LatLng(51.464239, 5.453679)))
        route882.add(Stop(title = "Калин"))
        route882.add(Stop(title = "Лазур"))
        route882.add(Stop(title = "КЗ"))
        route882.add(Stop(title = "8-ми декември", location = LatLng(43.203468, 27.900332)))
        route882.add(Stop(title = "Полиграфия"))
        route882.add(Stop(title = "Централна поща"))
        route882.add(Stop(title = "Нептун"))
        route882.add(Stop(title = "Л.к. Тракия"))
        route882.add(Stop(title = "Трансформатора"))
        route882.add(Stop(title = "Техникумите-Сливница"))
        route882.add(Stop(title = "8-ми септември"))
        route882.add(
            Stop(
                title = "Дом Младост /ул. Вяра за Вл-во/", location = LatLng(51.4652, 5.452)
            )
        )
        route882.add(Stop(title = "Централна автобаза"))
        route882.add(Stop(title = "Огоста"))
        route882.add(Stop(title = "Янтра"))
        route882.add(Stop(title = "Искър"))
        route882.add(Stop(title = "3-ти март"))
        route882.add(Stop(title = "Осъм"))
        route882.add(Stop(title = "ТИС Север"))
        route882.add(Stop(title = "Армейска"))
        route882.add(Stop(title = "Мургаш"))
        route882.add(Stop(title = "Вежен"))
        route882.add(Stop(title = "Детелина"))
        route882.add(Stop(title = "кап. Петко войвода 2"))
        route882.add(Stop(title = "Скалата"))
        route882.add(Stop(title = "Бл. 407 (Вл-во)"))

        val directions88: ArrayList<Direction> = ArrayList()
        directions88.add(Direction(title = "Аспарухово", route881, R.raw.asparuhovo))
        directions88.add(Direction(title = "Владиславово", route882, R.raw.vladislavovo))
        lines.add(Line(number = "88", directions = directions88, announcementFilePath = R.raw._88))

        val empty: ArrayList<Stop> = ArrayList()
        val directions999: ArrayList<Direction> = ArrayList()
        directions999.add(Direction(title = "ЗА ДЕПО", empty))
        directions999.add(Direction(title = "СЛУЖЕБЕН", empty))
        directions999.add(Direction(title = "ТЕСТ ДРАЙВ", empty))
        directions999.add(Direction(title = "УЧИЛИЩЕН", empty))
        lines.add(Line(number = "999", directions999))

        val route11: ArrayList<Stop> = ArrayList()
        route11.add(
            Stop(
                title = "ул. Brunelleschiweg",
                location = LatLng(51.465377, 5.452131),
                announcementFilePath = R.raw._8mi_dekemvri
            )
        )
        route11.add(Stop(title = "Светофара", location = LatLng(51.461966, 5.454300)))
        route11.add(Stop(title = "Джъмбо /Boschdijk/", location = LatLng(51.451085, 5.465096)))
        route11.add(Stop(title = "Пиаца", location = LatLng(51.441186, 5.476073)))

        val routeDepo: ArrayList<Stop> = ArrayList()
        routeDepo.add(Stop(title = "Пиаца", location = LatLng(51.441186, 5.476073)))
        routeDepo.add(Stop(title = "Джъмбо /Boschdijk/", location = LatLng(51.451085, 5.465096)))
        routeDepo.add(Stop(title = "Светофара", location = LatLng(51.461966, 5.454300)))
        routeDepo.add(
            Stop(
                title = "ул. Brunelleschiweg",
                location = LatLng(51.465377, 5.452131),
                announcementFilePath = R.raw._8mi_dekemvri
            )
        )

        val route12: ArrayList<Stop> = ArrayList()
        route12.add(
            Stop(
                title = "ул. Brunelleschiweg",
                location = LatLng(51.465377, 5.452131),
                announcementFilePath = R.raw._1
            )
        )
        route12.add(
            Stop(
                title = "Площадката",
                location = LatLng(51.464461, 5.453508),
                announcementFilePath = R.raw._2
            )
        )
        route12.add(
            Stop(
                title = "Кръстовище",
                location = LatLng(51.463444, 5.454460),
                announcementFilePath = R.raw._3
            )
        )
        route12.add(
            Stop(
                title = "Светофара",
                location = LatLng(51.462158, 5.454358),
                announcementFilePath = R.raw._4
            )
        )
        route12.add(
            Stop(
                title = "ул. Vignolaweg",
                location = LatLng(51.466371, 5.451120),
                announcementFilePath = R.raw._5
            )
        )

        val route13: ArrayList<Stop> = ArrayList()
        route13.add(
            Stop(
                title = "ул. Brunelleschiweg",
                location = LatLng(51.465377, 5.452131),
                announcementFilePath = R.raw._1
            )
        )
        route13.add(
            Stop(
                title = "Площадката",
                location = LatLng(51.464461, 5.453508),
                announcementFilePath = R.raw._2
            )
        )
        route13.add(
            Stop(
                title = "ул. Vignolaweg",
                location = LatLng(51.466371, 5.451120),
                announcementFilePath = R.raw._5
            )
        )

        val directions1: ArrayList<Direction> = ArrayList()
        directions1.add(Direction(title = "Пиаца", route11))
        directions1.add(Direction(title = "ЗА ДЕПО", routeDepo))
        directions1.add(Direction(title = "Тест", route12))
        directions1.add(Direction(title = "Тест 2", route13))
        lines.add(Line(number = "1", directions1, R.raw._1))

        return lines
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val line: Line = data()[position]
        val intent = Intent(this, ChooseDirectionActivity::class.java)
        intent.putExtra("line", line)
        startActivity(intent)
    }
}