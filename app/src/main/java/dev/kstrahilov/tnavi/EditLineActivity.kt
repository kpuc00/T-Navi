package dev.kstrahilov.tnavi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class EditLineActivity : AppCompatActivity() {
    private lateinit var line: Line
    private lateinit var etLineNumber: EditText
    private lateinit var btnChooseAudio: Button
    private lateinit var audioFileRow: LinearLayout
    private lateinit var tvSelectedAudioFile: TextView
    private var selectedAudioFileName: String? = null
    private var selectedFile: Uri? = null
    private val operations = Operations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_line)

        val intent = intent
        try {
            line = (intent.getParcelableExtra("line") as Line?)!!
        } catch (_: java.lang.NullPointerException) {
        }
        title = "${application.getString(R.string.label_edit_line)} ${line.number}"

        etLineNumber = findViewById(R.id.et_line_number)
        btnChooseAudio = findViewById(R.id.btn_choose_audio_line)
        audioFileRow = findViewById(R.id.audio_line_file_row)
        tvSelectedAudioFile = findViewById(R.id.tv_selected_audio_line_file)
        tvSelectedAudioFile.isSelected = true

        etLineNumber.setText(line.number)
        selectedAudioFileName = line.announcementFileName
        if (line.announcementFileName != "none") {
            tvSelectedAudioFile.text = line.announcementFileName
            audioFileRow.visibility = View.VISIBLE
        }
        btnChooseAudio.setOnClickListener {
            openFilePicker()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_save -> {
            line.number = etLineNumber.text.toString().trim()
            if (operations.updateLine(
                    applicationContext,
                    line,
                    selectedFile,
                    selectedAudioFileName
                )
            ) {
                finish()
            }

            true
        }
        else -> super.onOptionsItemSelected(item)
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
                    selectedFile = result.data!!.data!!
                    selectedAudioFileName =
                        operations.getFileNameFromUri(applicationContext, selectedFile).toString()
                    tvSelectedAudioFile.text = selectedAudioFileName
                    audioFileRow.visibility = View.VISIBLE
                }
            } else {
                return@registerForActivityResult
            }
        }
}