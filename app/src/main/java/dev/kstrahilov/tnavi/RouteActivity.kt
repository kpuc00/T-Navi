package dev.kstrahilov.tnavi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class RouteActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var tvLine: TextView
    private lateinit var tvDirection: TextView
    private lateinit var tvTimeColon: TextView
    private lateinit var lvRoute: ListView
    private lateinit var lineNumber: String
    private lateinit var direction: Direction
    private lateinit var route: ArrayList<Stop>
    private lateinit var stopListAdapter: StopListAdapter
    private lateinit var rowDateTime: LinearLayout
    private lateinit var tvNextStopInfoRow: TextView
    private lateinit var tvStopTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        hideSystemBars()
        supportActionBar?.hide()
        val intent = intent

        lineNumber = intent.getStringExtra("line").toString()
        direction = intent.getSerializableExtra("direction") as Direction

        tvLine = findViewById(R.id.tv_line)
        tvDirection = findViewById(R.id.tv_direction)
        tvTimeColon = findViewById(R.id.time_colon)
        lvRoute = findViewById(R.id.lv_route)
        rowDateTime = findViewById(R.id.row_datetime)
        tvNextStopInfoRow = findViewById(R.id.tv_next_stop_info_row)
        tvStopTitle = findViewById(R.id.tv_stop_title)

        route = direction.route
        stopListAdapter = StopListAdapter(
            applicationContext, route
        )
        lvRoute.adapter = stopListAdapter

        tvLine.text = lineNumber
        tvLine.isSelected = true

        tvDirection.text = direction.title
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

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(this, "Null received", Toast.LENGTH_SHORT).show()
                    } else {
//                        Toast.makeText(
//                            this, "Lat: " + location.latitude.toString() + ", Long: " +
//                                    location.longitude.toString(), Toast.LENGTH_LONG
//                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this, "Turn on location", Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}