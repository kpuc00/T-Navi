package dev.kstrahilov.tnavi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var btnStart: Button
    private lateinit var btnSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = application.getString(R.string.label_homePage)

        if (checkPermissions()) {
            requestPermission()
        }

        btnStart = findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            if (checkPermissions()) {
                val intent = Intent(this, ChooseLineActivity::class.java)
                startActivity(intent)
            } else {
                requestPermission()
            }
        }

        btnSettings=findViewById(R.id.btn_settings)
        btnSettings.setOnClickListener{
            val intent = Intent(this, CreateStop::class.java)
            startActivity(intent)
        }
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
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

        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(
                    this, "Нужно е разрешение за достъп до Файлове и мултимедия", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}