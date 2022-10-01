package dev.kstrahilov.tnavi

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.TextClock
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

class Route : AppCompatActivity() {

    private lateinit var tvLine: TextView
    private lateinit var tvDirection: TextView
    private lateinit var clock: TextClock
    private lateinit var weekday: TextView
    private lateinit var date: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        hideSystemBars()
        supportActionBar?.hide()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        tvLine = findViewById(R.id.tv_line)
        tvDirection = findViewById(R.id.tv_direction)
        clock = findViewById(R.id.clock)
        weekday = findViewById(R.id.tv_weekday)
        date = findViewById(R.id.tv_date)

        tvLine.isSelected = true
        tvDirection.isSelected = true

        clock.format24Hour = "hh:mm"

        val currentDate = Calendar.getInstance().time
        val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentDate)
        val splitDate = formattedDate.toString().split(",")
        weekday.text = splitDate[0].trim()
        date.text = splitDate[1].trim()
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