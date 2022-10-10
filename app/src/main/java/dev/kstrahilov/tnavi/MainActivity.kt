package dev.kstrahilov.tnavi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnStart: Button
    private lateinit var btnSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = application.getString(R.string.label_homePage)

        btnStart = findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            val intent = Intent(this, ChooseLineActivity::class.java)
            startActivity(intent)
        }

        btnSettings = findViewById(R.id.btn_settings)
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}