package dev.kstrahilov.tnavi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
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
import com.google.android.gms.location.*

class RouteActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var gpsStatus: ImageView
    private lateinit var locationCallback: LocationCallback
    private lateinit var loadingScreen: LinearLayout
    private lateinit var logoLoading: ImageView
    private lateinit var tvLine: TextView
    private lateinit var tvDirection: TextView
    private lateinit var tvTimeColon: TextView
    private lateinit var lvRoute: ListView
    private lateinit var lineNumber: String
    private var lineAnnouncement: Int = 0
    private lateinit var direction: Direction
    private var directionAnnouncement: Int = 0
    private lateinit var route: ArrayList<Stop>
    private lateinit var loadedRoute: ArrayList<Stop>
    private lateinit var stopListAdapter: StopListAdapter
    private lateinit var rowDateTime: LinearLayout
    private lateinit var rowDateTimeAnim: Animation
    private lateinit var tvNextStopInfoRow: TextView
    private lateinit var tvStopTitle: TextView
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val operations = Operations()

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        private const val UPDATE_DEFAULT_INTERVAL = 5L
        private const val FAST_UPDATE_INTERVAL = 5L
        private const val DESTINATION_RADIUS = 50
    }

    override fun onBackPressed() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(application.getString(R.string.exit_route))
        alert.setMessage(application.getString(R.string.exit_route_question))
        alert.setPositiveButton(application.getString(R.string.yes)) { _, _ ->
            stopGPSUpdates()
            mediaPlayer.release()
            super.onBackPressed()
        }
        alert.setNegativeButton(application.getString(R.string.no)) { _, _ -> }
        val alertDialog = alert.create()
        alertDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        hideSystemBars()
        supportActionBar?.hide()

        val intent = intent
        lineNumber = intent.getStringExtra("line").toString()
        val strLineAnnouncement = intent.getStringExtra("lineAnnouncement")
        lineAnnouncement = if (strLineAnnouncement != null && strLineAnnouncement != "null") {
            strLineAnnouncement.toInt()
        } else 0

        direction = intent.getParcelableExtra<Direction>("direction") as Direction
        directionAnnouncement = if (direction.announcementFilePath != null) {
            direction.announcementFilePath!!
        } else 0

        gpsStatus = findViewById(R.id.gps_status)
        if (!checkPermissions()) {
            requestPermission()
        }

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

        route = ArrayList(
            operations.convertListOfStopIdsToListOfStops(
                direction.routeStopIds, applicationContext
            )
        )
        loadedRoute = ArrayList(route)
        populateListView(loadedRoute)

        announce(R.raw.liniq)
        mediaPlayer.setOnCompletionListener {
            if (lineAnnouncement != 0) {
                announce(lineAnnouncement)
                mediaPlayer.setOnCompletionListener {
                    announce(R.raw.posoka)
                    mediaPlayer.setOnCompletionListener {
                        announce(directionAnnouncement)
                    }
                }
            } else {
                announce(R.raw.posoka)
                mediaPlayer.setOnCompletionListener {
                    announce(directionAnnouncement)
                }
            }
        }

        if (loadedRoute.size > 0) {
            val stop = loadedRoute[0]
            stop.isNext = true
            updateStopInfoRow(stop)

            startGPSUpdates()
        } else {
            tvStopTitle.text = ""
        }

        val clockAnim: Animation = AlphaAnimation(0.0f, 1.0f)
        clockAnim.duration = 1
        clockAnim.startOffset = 1000
        clockAnim.repeatMode = Animation.REVERSE
        clockAnim.repeatCount = Animation.INFINITE
        tvTimeColon.startAnimation(clockAnim)

        rowDateTimeAnim = AlphaAnimation(0.0f, 1.0f)
        rowDateTimeAnim.duration = 1
        rowDateTimeAnim.startOffset = 5000
        rowDateTimeAnim.repeatMode = Animation.REVERSE
        rowDateTimeAnim.repeatCount = Animation.INFINITE
        rowDateTime.startAnimation(rowDateTimeAnim)
    }

    private fun populateListView(content: ArrayList<Stop>) {
        stopListAdapter = StopListAdapter(
            applicationContext, content
        )
        lvRoute.adapter = stopListAdapter
        lvRoute.onItemClickListener = this
    }

    private fun updateStopInfoRow(stop: Stop) {
        tvStopTitle.isSelected = true
        if (stop.isCurrent) {
            tvStopTitle.text = stop.toString()
            tvNextStopInfoRow.text = tvNextStopInfoRow.context.getText(R.string.stop_)
            tvStopTitle.setTextColor(tvStopTitle.context.getColor(R.color.red))
        } else {
            tvStopTitle.text = stop.toString()
            tvNextStopInfoRow.text = tvNextStopInfoRow.context.getText(R.string.next_stop_)
            tvStopTitle.setTextColor(tvStopTitle.context.getColor(R.color.green))
        }
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
        if (audio != 0) {
            mediaPlayer = MediaPlayer.create(this, audio)
            mediaPlayer.start()
        }
    }

    private fun startGPSUpdates() {
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
                locationRequest = LocationRequest.create().apply {
                    interval = 1000 * UPDATE_DEFAULT_INTERVAL
                    fastestInterval = 1000 * FAST_UPDATE_INTERVAL
                    priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
                }

                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        val currentLocation: Location? = locationResult.lastLocation
                        if (currentLocation != null) {
                            route.forEach { stop ->
                                val index = route.indexOf(stop)

                                val distance = FloatArray(1)
                                Location.distanceBetween(
                                    currentLocation.latitude,
                                    currentLocation.longitude,
                                    stop.location.latitude,
                                    stop.location.longitude,
                                    distance
                                )

                                //Announce next stop
                                if (loadedRoute.size > 1 && distance[0] > DESTINATION_RADIUS && stop.isCurrent && !mediaPlayer.isPlaying) {
                                    stop.isCurrent = false
                                    val nextStop = route[index + 1]
                                    loadedRoute.removeAt(0)
                                    populateListView(loadedRoute)
                                    updateStopInfoRow(nextStop)
                                    rowDateTimeAnim.startNow()
                                    announce(R.raw.sledvashta_spirka)
                                    mediaPlayer.setOnCompletionListener {
                                        if (nextStop.announcementFilePath != null) {
                                            announce(nextStop.announcementFilePath!!)
                                        }
                                    }
                                }

                                //Announce current stop
                                if (distance[0] < DESTINATION_RADIUS && !stop.isAnnounced && !mediaPlayer.isPlaying) {
                                    loadedRoute[0].isCurrent = false
                                    if (loadedRoute.size > 1) {
                                        loadedRoute[1].isNext = false
                                    }
                                    loadedRoute[0].isNext = false
                                    loadedRoute = ArrayList(route.drop(index))
                                    populateListView(loadedRoute)
                                    loadedRoute[0].isCurrent = true
                                    loadedRoute[0].isNext = false
                                    if (route.indexOf(stop) < route.size - 1) {
                                        loadedRoute[1].isNext = true
                                    }
                                    updateStopInfoRow(stop)
                                    rowDateTimeAnim.startNow()
                                    announce(R.raw.spirka)
                                    mediaPlayer.setOnCompletionListener {
                                        if (stop.announcementFilePath != null) {
                                            announce(stop.announcementFilePath!!)
                                        }
                                    }
                                    stop.isAnnounced = true
                                    loadedRoute[0].isAnnounced = true
                                } else {
                                    return@forEach
                                }
                            }
                        }
                    }

                }

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback, null
                )
            } else {
                Toast.makeText(
                    this, application.getString(R.string.enable_location), Toast.LENGTH_LONG
                ).show()
            }
        } else {
            requestPermission()
        }
    }

    private fun stopGPSUpdates() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        } catch (_: UninitializedPropertyAccessException) {
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val stop: Stop = loadedRoute[position]
        if (!mediaPlayer.isPlaying) {
            stop.announcementFilePath?.let { announce(it) }
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
                gpsStatus.visibility = View.GONE
                startGPSUpdates()
            } else {
                gpsStatus.visibility = View.VISIBLE
                Toast.makeText(
                    applicationContext,
                    application.getString(R.string.location_access_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}