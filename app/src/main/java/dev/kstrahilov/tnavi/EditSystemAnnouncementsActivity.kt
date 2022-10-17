package dev.kstrahilov.tnavi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class EditSystemAnnouncementsActivity : AppCompatActivity() {
    private lateinit var btnFileLine: Button
    private lateinit var tvFileLine: TextView
    private lateinit var btnFileDirection: Button
    private lateinit var tvFileDirection: TextView
    private lateinit var btnFileNextStop: Button
    private lateinit var tvFileNextStop: TextView
    private lateinit var btnFileStop: Button
    private lateinit var tvFileStop: TextView
    private lateinit var announcements: ArrayList<Announcement>
    private var selectedUpload = ""
    private var operations = Operations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_system_announcements)
        title = application.getString(R.string.system_announcements)

        btnFileLine = findViewById(R.id.btn_file_line)
        tvFileLine = findViewById(R.id.tv_file_line)
        btnFileDirection = findViewById(R.id.btn_file_direction)
        tvFileDirection = findViewById(R.id.tv_file_direction)
        btnFileNextStop = findViewById(R.id.btn_file_next_stop)
        tvFileNextStop = findViewById(R.id.tv_file_next_stop)
        btnFileStop = findViewById(R.id.btn_file_stop)
        tvFileStop = findViewById(R.id.tv_file_stop)

        announcements = operations.loadSystemAnnouncementsFromInternalStorage(applicationContext)
        announcements.forEach {
            when (it.title) {
                "line" -> {
                    tvFileLine.text = it.announcementFileName
                }
                "direction" -> {
                    tvFileDirection.text = it.announcementFileName
                }
                "next_stop" -> {
                    tvFileNextStop.text = it.announcementFileName
                }
                "stop" -> {
                    tvFileStop.text = it.announcementFileName
                }
            }
        }

        btnFileLine.setOnClickListener {
            selectedUpload = "line"
            openFilePicker()
        }
        btnFileDirection.setOnClickListener {
            selectedUpload = "direction"
            openFilePicker()
        }
        btnFileNextStop.setOnClickListener {
            selectedUpload = "next_stop"
            openFilePicker()
        }
        btnFileStop.setOnClickListener {
            selectedUpload = "stop"
            openFilePicker()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                if (result.data != null) {
                    val selectedFile = result.data!!.data!!
                    val fileName = operations.storeSystemAnnouncement(
                        applicationContext, selectedUpload, selectedFile
                    )
                    when (selectedUpload) {
                        "line" -> {
                            tvFileLine.text = fileName
                            selectedUpload = ""
                        }
                        "direction" -> {
                            tvFileDirection.text = fileName
                            selectedUpload = ""
                        }
                        "next_stop" -> {
                            tvFileNextStop.text = fileName
                            selectedUpload = ""
                        }
                        "stop" -> {
                            tvFileStop.text = fileName
                            selectedUpload = ""
                        }
                    }
                }
            } else {
                return@registerForActivityResult
            }
        }
}