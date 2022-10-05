package dev.kstrahilov.tnavi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class RouteActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private var currentLocation: Location? = null

    private lateinit var loadingScreen: LinearLayout
    private lateinit var logoLoading: ImageView
    private lateinit var tvLine: TextView
    private lateinit var tvDirection: TextView
    private lateinit var tvTimeColon: TextView
    private lateinit var lvRoute: ListView
    private lateinit var lineNumber: String
    private var lineAnnouncement: Int? = null
    private lateinit var direction: Direction
    private lateinit var route: ArrayList<Stop>
    private lateinit var stopListAdapter: StopListAdapter
    private lateinit var rowDateTime: LinearLayout
    private lateinit var tvNextStopInfoRow: TextView
    private lateinit var tvStopTitle: TextView
    private var mediaPlayer: MediaPlayer = MediaPlayer()
//    private var audioList: ArrayList<Int> = ArrayList()

    override fun onBackPressed() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(application.getString(R.string.exit_route))
        alert.setMessage(application.getString(R.string.exit_route_question))
        alert.setPositiveButton(application.getString(R.string.yes)) { _, _ ->
            mediaPlayer.release()
            finish()
        }
        alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
        val alertDialog = alert.create()
        alertDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        hideSystemBars()
        supportActionBar?.hide()
        val intent = intent

        lineNumber = intent.getStringExtra("line").toString()
        val strLineAnnouncement = intent.getStringExtra("lineAnnouncement")
        lineAnnouncement = if (strLineAnnouncement != null && strLineAnnouncement != "null") {
            strLineAnnouncement.toInt()
        } else {
            null
        }
        direction = intent.getSerializableExtra("direction") as Direction

        loadingScreen = findViewById(R.id.loading_screen)
        logoLoading = findViewById(R.id.logo_loading)
        logoLoading.postDelayed({ logoLoading.visibility = View.VISIBLE }, 2000)
        loadingScreen.postDelayed({
            loadingScreen.visibility = View.GONE
            initializeApp()
        }, 10000)

    }

    private fun initializeApp() {
        tvLine = findViewById(R.id.tv_line)
        tvDirection = findViewById(R.id.tv_direction)
        tvTimeColon = findViewById(R.id.time_colon)
        lvRoute = findViewById(R.id.lv_route)
        rowDateTime = findViewById(R.id.row_datetime)
        tvNextStopInfoRow = findViewById(R.id.tv_next_stop_info_row)
        tvStopTitle = findViewById(R.id.tv_stop_title)

        tvLine.text = lineNumber
        tvLine.isSelected = true

        tvDirection.text = direction.title
        tvDirection.isSelected = true

        route = direction.route
        stopListAdapter = StopListAdapter(
            applicationContext, route
        )
        lvRoute.adapter = stopListAdapter

        announce(R.raw.liniq)
        mediaPlayer.setOnCompletionListener {
            if (lineAnnouncement !== null) {
                announce(lineAnnouncement!!)
            }
            mediaPlayer.setOnCompletionListener {
                announce(R.raw.posoka)
                mediaPlayer.setOnCompletionListener {
                    if (direction.announcementFilePath !== null) {
                        announce(direction.announcementFilePath!!)
                    }
                }
            }
        }

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

    private fun announce(audio: Int) {
        mediaPlayer = MediaPlayer.create(this, audio)
        mediaPlayer.start()
    }


    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
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
                    this, application.getString(R.string.enable_location), Toast.LENGTH_SHORT
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
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(
                    applicationContext,
                    application.getString(R.string.location_access_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}