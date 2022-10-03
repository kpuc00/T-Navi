package dev.kstrahilov.tnavi

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class RouteActivity : AppCompatActivity() {
    private lateinit var tvLine: TextView
    private lateinit var tvDirection: TextView
    private lateinit var tvTimeColon: TextView
    private lateinit var lvRoute: ListView
    private lateinit var line: Line
    private lateinit var route: ArrayList<Stop>
    private lateinit var stopListAdapter: StopListAdapter
    private lateinit var rowDateTime: LinearLayout
    private lateinit var tvNextStopInfoRow: TextView
    private lateinit var tvStopTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        hideSystemBars()
        supportActionBar?.hide()

        line = setData()

        tvLine = findViewById(R.id.tv_line)
        tvDirection = findViewById(R.id.tv_direction)
        tvTimeColon = findViewById(R.id.time_colon)
        lvRoute = findViewById(R.id.lv_route)
        rowDateTime = findViewById(R.id.row_datetime)
        tvNextStopInfoRow = findViewById(R.id.tv_next_stop_info_row)
        tvStopTitle = findViewById(R.id.tv_stop_title)

        route = line.directions[0].route
        stopListAdapter = StopListAdapter(
            applicationContext, route
        )
        lvRoute.adapter = stopListAdapter

        tvLine.text = line.toString()
        tvLine.isSelected = true

        tvDirection.text = line.directions[0].toString()
        tvDirection.isSelected = true

        if (route.size > 0) {
            val stop = route[0]
            tvStopTitle.text = stop.toString()
            tvStopTitle.isSelected = true
            if (stop.isCurrent) {
                tvNextStopInfoRow.text = tvNextStopInfoRow.context.getText(R.string.stop_)
                tvStopTitle.setTextColor(tvStopTitle.context.getColor(R.color.red))
            } else {
                tvNextStopInfoRow.text = tvNextStopInfoRow.context.getText(R.string.next_stop_)
                tvStopTitle.setTextColor(tvStopTitle.context.getColor(R.color.green))
            }
        } else {
            tvStopTitle.text = ""
        }

        val clockAnim: Animation = AlphaAnimation(0.0f, 1.0f)
        clockAnim.duration = 1
        clockAnim.startOffset = 1000
        clockAnim.repeatMode = Animation.REVERSE
        clockAnim.repeatCount = Animation.INFINITE
        tvTimeColon.startAnimation(clockAnim)

        val rowDateTimeAnim: Animation = AlphaAnimation(0.0f, 1.0f)
        rowDateTimeAnim.duration = 1
        rowDateTimeAnim.startOffset = 5000
        rowDateTimeAnim.repeatMode = Animation.REVERSE
        rowDateTimeAnim.repeatCount = Animation.INFINITE
        rowDateTime.startAnimation(rowDateTimeAnim)

    }

    private fun setData(): Line {
        val route: ArrayList<Stop> = ArrayList()
        route.add(Stop("Бл. 407 (Вл-во)", true, false, null, null))
        route.add(Stop("кап. Петко войвода 2", false, true, null, null))
        route.add(Stop("Детелина", false, false, null, null))
        route.add(Stop("Вежен", false, false, null, null))
        route.add(Stop("Мургаш", false, false, null, null))
        route.add(Stop("Армейска", false, false, null, null))
        route.add(Stop("ТИС Север", false, false, null, null))
        route.add(Stop("Осъм", false, false, null, null))
        route.add(Stop("3-ти март", false, false, null, null))
        route.add(Stop("Искър", false, false, null, null))
        route.add(Stop("Янтра", false, false, null, null))
        route.add(Stop("Огоста", false, false, null, null))
        route.add(Stop("Централна автобаза", false, false, null, null))
        route.add(Stop("Дом Младост (за Аспарухово)", false, false, null, null))
        route.add(Stop("8-ми септември", false, false, null, null))
        route.add(Stop("Техникумите-Сливница", false, false, null, null))
        route.add(Stop("Трансформатора", false, false, null, null))
        route.add(Stop("Л.к. Тракия", false, false, null, null))
        route.add(Stop("Нептун", false, false, null, null))
        route.add(Stop("Централна поща (за Аспарухово)", false, true, null, null))
        route.add(Stop("Полиграфия", false, false, null, null))
        route.add(Stop("8-ми декември", false, false, null, null))
        route.add(Stop("КЗ", false, false, null, null))
        route.add(Stop("ИХА", false, false, null, null))
        route.add(Stop("Лазур", false, false, null, null))
        route.add(Stop("Калин", false, false, null, null))
        route.add(Stop("Любен Каравелов", false, false, null, null))
        route.add(Stop("Народни Будители", false, false, null, null))
        route.add(Stop("Обръщач тролеи", false, false, null, null))

        val directions: ArrayList<Direction> = ArrayList()
        directions.add(Direction("Аспарухово", route))

        return Line("88", directions)

    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}