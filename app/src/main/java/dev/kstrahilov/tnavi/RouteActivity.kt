package dev.kstrahilov.tnavi

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
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
    private var lvRoute: ListView? = null
    private var stopListAdapter: StopListAdapter? = null
    private var route: ArrayList<Stop>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        hideSystemBars()
        supportActionBar?.hide()

        tvLine = findViewById(R.id.tv_line)
        tvDirection = findViewById(R.id.tv_direction)
        tvTimeColon = findViewById(R.id.time_colon)
        lvRoute = findViewById(R.id.lv_route)

        route = ArrayList()
        route = setDataRoute()
        stopListAdapter = StopListAdapter(
            applicationContext, route!!
        )
        lvRoute?.adapter = stopListAdapter

        tvLine.isSelected = true
        tvDirection.isSelected = true

        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1
        anim.startOffset = 1000
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        tvTimeColon.startAnimation(anim)
    }

    private fun setDataRoute(): ArrayList<Stop> {
        val route: ArrayList<Stop> = ArrayList()
        route.add(Stop("8-ми декември", true, false))
        route.add(Stop("КЗ", false, true))
        route.add(Stop("ИХА", false, false))
        route.add(Stop("Лазур", false, false))
        route.add(Stop("Калин", false, false))
        route.add(Stop("Любен Каравелов", false, false))
        route.add(Stop("Народни Будители", false, false))
        route.add(Stop("Обръщач тролеи", false, false))

        return route

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