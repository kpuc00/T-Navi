package dev.kstrahilov.tnavi

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileNotFoundException
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Operations {
    var gson = Gson()

    companion object {
        private const val STORAGE_PATH = "storage"
        private const val AUDIO_PATH = "${STORAGE_PATH}/audio"
        private const val STOPS_FILE_NAME = "stops.json"
        private const val LINES_FILE_NAME = "lines.json"
        private const val EXPORTED_FILE_EXTENSION = ".tnav"
    }

    fun loadStopsFromInternalStorage(applicationContext: Context): ArrayList<Stop> {
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$STOPS_FILE_NAME"
        val file = File(directory.path, fileName)

        return if (file.exists()) {
            val readJson = file.readText(Charsets.UTF_8)
            val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
            gson.fromJson(readJson, stopListType)
        } else {
            ArrayList()
        }
    }

//    fun loadStopsJSONFromInternalStorage(applicationContext: Context): String {
//        val path: String = applicationContext.filesDir.toString()
//        val directory = File("$path/$STORAGE_PATH")
//        directory.mkdir()
//        val fileName = "/$STOPS_FILE_NAME"
//        val file = File(directory.path, fileName)
//
//        return if (file.exists()) {
//            file.readText(Charsets.UTF_8)
//        } else {
//            "[]"
//        }
//    }

    fun loadLinesFromInternalStorage(applicationContext: Context): ArrayList<Line> {
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$LINES_FILE_NAME"
        val file = File(directory.path, fileName)

        return if (file.exists()) {
            val readJson = file.readText(Charsets.UTF_8)
            val stopListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
            gson.fromJson(readJson, stopListType)
        } else {
            ArrayList()
        }
    }

    fun convertListOfStopIdsToListOfStops(
        stopIds: ArrayList<UUID>, applicationContext: Context
    ): ArrayList<Stop> {
        val stops = loadStopsFromInternalStorage(applicationContext)
        val stopsToReturn: ArrayList<Stop> = ArrayList()

        stopIds.forEach { uuid ->
            val stop = stops.find { it.id == uuid }
            if (stop != null) {
                stopsToReturn.add(stop)
            }
        }

        return stopsToReturn
    }

    fun convertListOfStopsToListOfStopIds(stops: ArrayList<Stop>): ArrayList<UUID> {
        return stops.map { it.id } as ArrayList<UUID>
    }

    fun removeDeletedStopIdsFromRoutes(applicationContext: Context) {
        val stops = loadStopsFromInternalStorage(applicationContext)
        val lines = loadLinesFromInternalStorage(applicationContext)
        lines.forEach { line ->
            line.directions!!.forEach { direction ->
                direction.routeStopIds.removeIf { uuid ->
                    stops.find { it.id == uuid } == null
                }
            }
        }
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$LINES_FILE_NAME"
        val file = File(directory.path, fileName)
        val jsonString: String = gson.toJson(lines)
        file.writeText(jsonString, Charsets.UTF_8)
    }

    private fun importAudioFileToInternalStorage(
        applicationContext: Context, uri: Uri?, objectId: UUID
    ): String {
        if (uri != null) {
            try {
                applicationContext.contentResolver.openInputStream(uri)!!.use {
                    val inputStream = ByteArrayInputStream(it.readBytes())
                    val path: String = applicationContext.filesDir.toString()
                    val directory = File("$path/$AUDIO_PATH")
                    directory.mkdir()
                    val subStr = objectId.toString().substringBefore("-")
                    val fileName = "${subStr}_${getFileNameFromUri(applicationContext, uri)}"
                    val file = File(directory.path, fileName!!)
                    inputStream.use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    return fileName
                }
            } catch (_: FileNotFoundException) {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.file_upload_error_message),
                    Toast.LENGTH_LONG
                ).show()
                return "error"
            }
        }
        return "error"
    }

    private fun deleteOldAudioFile(applicationContext: Context, fileName: String): Boolean {
        val path: String = applicationContext.filesDir.toString()
        val file = File("$path/$AUDIO_PATH", "/$fileName")
        return if (file.exists()) {
            file.delete()
        } else false
    }

//    fun loadLinesJSONFromInternalStorage(applicationContext: Context): String {
//        val path: String = applicationContext.filesDir.toString()
//        val directory = File("$path/$STORAGE_PATH")
//        directory.mkdir()
//        val file = File(directory.path, "/$LINES_FILE_NAME")
//
//        return if (file.exists()) {
//            file.readText(Charsets.UTF_8)
//        } else {
//            "[]"
//        }
//    }

    fun saveStop(
        applicationContext: Context,
        stop: Stop?,
        stops: ArrayList<Stop>,
        selectedFile: Uri?,
        selectedAudioFileName: String?
    ): Boolean {
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$STOPS_FILE_NAME"
        val file = File(directory.path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
                val readStops: ArrayList<Stop> = gson.fromJson(readJson, stopListType)

                //Remove old stop to keep only the new one
                if (stop != null) {
                    val readStop = readStops.filter {
                        it.id == stop.id
                    }[0]
                    readStops.remove(readStop)
                }
                stops.addAll(readStops)
            }
            if (selectedFile != null && selectedAudioFileName != null) {
                if (stops[0].announcementFileName != selectedAudioFileName) {
                    deleteOldAudioFile(applicationContext, stops[0].announcementFileName)
                    val finalFileName = importAudioFileToInternalStorage(
                        applicationContext,
                        selectedFile,
                        stops[0].id
                    )
                    if (finalFileName != "error") {
                        stops[0].announcementFileName = finalFileName
                    }
                }
            }
            val currentStop = stops[0]
            stops.sortBy { it.title.lowercase() }
            val jsonString = gson.toJson(stops)
            file.writeText(jsonString, Charsets.UTF_8)
            return checkIfStopDataWritten(
                applicationContext, file, jsonString, currentStop.title, stop
            )
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return false
        }
    }

    private fun checkIfStopDataWritten(
        applicationContext: Context,
        file: File,
        jsonString: String,
        stopTitleToDisplay: String,
        stop: Stop?
    ): Boolean {
        if (file.readText(Charsets.UTF_8) == jsonString) {
            if (stop != null) {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.stop_)} $stopTitleToDisplay ${
                        applicationContext.getString(R.string.was_modified)
                    }",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.stop_)} $stopTitleToDisplay ${
                        applicationContext.getString(R.string.was_created)
                    }",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return true
        } else {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
    }

    fun deleteStop(applicationContext: Context, stop: Stop?) {
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$STOPS_FILE_NAME"
        val file = File(directory.path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
                val readStops: ArrayList<Stop> = gson.fromJson(readJson, stopListType)

                //Remove old stop to keep only the new one
                if (stop != null) {
                    val readStop = readStops.filter {
                        it.id == stop.id
                    }[0]
                    readStops.remove(readStop)
                    val jsonString: String = gson.toJson(readStops)
                    file.writeText(jsonString, Charsets.UTF_8)
                    Toast.makeText(
                        applicationContext,
                        "${applicationContext.getString(R.string.stop_)} ${stop.title} ${
                            applicationContext.getString(R.string.was_deleted)
                        }",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    fun saveDirection(
        applicationContext: Context,
        lineId: String?,
        direction: Direction?,
        newDirection: Direction,
        selectedFile: Uri?,
        selectedAudioFileName: String?
    ): Boolean {
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$LINES_FILE_NAME"
        val file = File(directory.path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val linesListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
                val readLines: ArrayList<Line> = gson.fromJson(readJson, linesListType)
                val currentLine: Line = readLines.filter { it.id.toString() == lineId }[0]

                //Updating the line with new a direction
                //First remove it and add it again when updated
                readLines.remove(currentLine)
                //Remove old direction to keep only the new one
                if (direction != null) {
                    val readDirection = currentLine.directions!!.filter {
                        it.id.toString() == direction.id.toString()
                    }[0]
                    currentLine.directions!!.remove(readDirection)
                }
                if (selectedFile != null && selectedAudioFileName != null) {
                    if (newDirection.announcementFileName != selectedAudioFileName) {
                        deleteOldAudioFile(applicationContext, newDirection.announcementFileName)
                        val finalFileName = importAudioFileToInternalStorage(
                            applicationContext,
                            selectedFile,
                            newDirection.id
                        )
                        if (finalFileName != "error") {
                            newDirection.announcementFileName = finalFileName
                        }
                    }
                }
                currentLine.directions!!.add(newDirection)
                currentLine.directions!!.sortBy { it.title.lowercase() }
                readLines.add(currentLine)
                readLines.sortBy { it.number }

                val jsonString: String = gson.toJson(readLines)
                file.writeText(jsonString, Charsets.UTF_8)
                checkIfDirectionDataWritten(
                    applicationContext, file, jsonString, newDirection.title, direction
                )
                return true
            } else {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return false
        }
    }

    fun deleteDirection(
        applicationContext: Context, lineId: String?, direction: Direction
    ): Boolean {
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/storage")
        directory.mkdir()
        val fileName = "/lines.json"
        val file = File(directory.path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val linesListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
                val readLines: ArrayList<Line> = gson.fromJson(readJson, linesListType)
                val currentLine: Line = readLines.filter { it.id.toString() == lineId }[0]

                //Updating the line with removed direction
                //First remove it and add it again when updated
                readLines.remove(currentLine)
                //Remove direction
                val readDirection = currentLine.directions!!.filter {
                    it.id.toString() == direction.id.toString()
                }[0]
                currentLine.directions!!.remove(readDirection)
                readLines.add(currentLine)
                readLines.sortBy { it.number }

                val jsonString: String = gson.toJson(readLines)
                file.writeText(jsonString, Charsets.UTF_8)
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.direction_)} ${direction.title} ${
                        applicationContext.getString(R.string.was_deleted)
                    }",
                    Toast.LENGTH_SHORT
                ).show()

                return true
            } else {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return false
        }
    }

    private fun checkIfDirectionDataWritten(
        applicationContext: Context,
        file: File,
        jsonString: String,
        directionTitleToDisplay: String,
        direction: Direction?
    ): Boolean {
        if (file.readText(Charsets.UTF_8) == jsonString) {
            if (direction != null) {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.direction_)} $directionTitleToDisplay ${
                        applicationContext.getString(R.string.was_modified)
                    }",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.direction_)} $directionTitleToDisplay ${
                        applicationContext.getString(R.string.was_created)
                    }",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return true
        } else {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
    }

    fun updateLine(
        applicationContext: Context,
        line: Line,
        selectedFile: Uri?,
        selectedAudioFileName: String?
    ): Boolean {
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$LINES_FILE_NAME"
        val file = File(directory.path, fileName)
        val lines: ArrayList<Line> = ArrayList()

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val stopListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
                val readLines: ArrayList<Line> = gson.fromJson(readJson, stopListType)

                //Remove old line to keep only the new one
                val readLine = readLines.filter {
                    it.id == line.id
                }[0]
                readLines.remove(readLine)
                lines.addAll(readLines)
            }
            if (lines.any { it.number == line.number }) {
                val alert = AlertDialog.Builder(applicationContext)
                alert.setTitle(applicationContext.getString(R.string.error))
                alert.setMessage(applicationContext.getString(R.string.line_exists))
                alert.setNegativeButton(applicationContext.getString(R.string.ok)) { _, _ -> }
                val alertDialog = alert.create()
                alertDialog.show()
                return false
            }
            if (selectedFile != null && selectedAudioFileName != null) {
                if (line.announcementFileName != selectedAudioFileName) {
                    deleteOldAudioFile(applicationContext, line.announcementFileName)
                    val finalFileName = importAudioFileToInternalStorage(
                        applicationContext,
                        selectedFile,
                        line.id
                    )
                    if (finalFileName != "error") {
                        line.announcementFileName = finalFileName
                        lines.add(line)
                    }
                }
            }
            lines.sortBy { it.number.lowercase() }
            val jsonString = gson.toJson(lines)
            file.writeText(jsonString, Charsets.UTF_8)
            Toast.makeText(
                applicationContext,
                "${applicationContext.getString(R.string.line_)} ${line.number} ${
                    applicationContext.getString(
                        R.string.was_modified
                    )
                }",
                Toast.LENGTH_SHORT
            ).show()
            return true
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return false
        }
    }

    fun saveLine(applicationContext: Context, line: Line?, newLine: Line): Boolean {
        val lines: ArrayList<Line> = ArrayList()
        val path: String = applicationContext.filesDir.toString()
        val directory = File("$path/$STORAGE_PATH")
        directory.mkdir()
        val fileName = "/$LINES_FILE_NAME"
        val file = File(directory.path, fileName)

        try {
            if (file.exists()) {
                val readJson = file.readText(Charsets.UTF_8)
                val linesListType: Type = object : TypeToken<ArrayList<Line?>?>() {}.type
                val readLines: ArrayList<Line> = gson.fromJson(readJson, linesListType)
                if (line != null) {
                    val currentLine: Line =
                        readLines.filter { it.id.toString() == line.id.toString() }[0]

                    //Updating the line
                    //First remove it and add it again when updated
                    readLines.remove(currentLine)
                    lines.add(newLine)
                    lines.addAll(readLines)
                } else {
                    if (readLines.any { it.number == newLine.number }) {
                        val alert = AlertDialog.Builder(applicationContext)
                        alert.setTitle(applicationContext.getString(R.string.error))
                        alert.setMessage(applicationContext.getString(R.string.line_exists))
                        alert.setNegativeButton(applicationContext.getString(R.string.ok)) { _, _ -> }
                        val alertDialog = alert.create()
                        alertDialog.show()
                        return false
                    } else {
                        lines.add(newLine)
                        lines.addAll(readLines)
                    }
                }
            }
            lines.sortBy { it.number }
            val jsonString: String = gson.toJson(lines)
            file.writeText(jsonString, Charsets.UTF_8)
            return checkIfLineDataWritten(
                applicationContext, file, jsonString, newLine.number, line
            )
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return false
        }
    }

    private fun checkIfLineDataWritten(
        applicationContext: Context,
        file: File,
        jsonString: String,
        lineNumberToDisplay: String,
        line: Line?
    ): Boolean {
        if (file.readText(Charsets.UTF_8) == jsonString) {
            if (line != null) {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.line_)} $lineNumberToDisplay ${
                        applicationContext.getString(R.string.was_modified)
                    }",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "${applicationContext.getString(R.string.line_)} $lineNumberToDisplay ${
                        applicationContext.getString(R.string.was_created)
                    }",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return true
        } else {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
    }

//    fun saveStopsToInternalStorageFromFile(applicationContext: Context, file: File): Boolean {
//        val stopsData: String?
//        val path: String = applicationContext.filesDir.toString()
//        val directory = File("$path/$STORAGE_PATH")
//        directory.mkdir()
//        val internalFile = File(directory.path, "/$STOPS_FILE_NAME")
//
//        stopsData = file.readText(Charsets.UTF_8)
//        val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
//        val readStops: ArrayList<Stop> = gson.fromJson(stopsData, stopListType)
//        val jsonString: String = gson.toJson(readStops)
//        internalFile.writeText(jsonString, Charsets.UTF_8)
//        return true
//    }

    fun exportDataToExternalStorage(
        applicationContext: Context, application: Application, json: String
    ): Boolean {
        val externalPath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .toString()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val externalFileName =
            application.getString(R.string.app_name) + "/data_" + LocalDateTime.now()
                .format(dateFormatter) + EXPORTED_FILE_EXTENSION
        val externalFile = File(externalPath, externalFileName)
        val externalDirectory = File(application.getString(R.string.app_name))

        try {
            if (!externalDirectory.exists()) {
                externalDirectory.mkdir()
            }
            externalFile.parentFile?.mkdir()
            externalFile.writeText(json, Charsets.UTF_8)
            Toast.makeText(
                applicationContext,
                application.getString(R.string.data_exported),
                Toast.LENGTH_SHORT
            ).show()
            return true
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.error_message),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
            return false
        }
    }

    fun getFileNameFromUri(applicationContext: Context, uri: Uri?): String? {
        var fileName: String? = null
        if (uri != null) {
            applicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                fileName = cursor.getString(nameIndex)
            }
        }
        return fileName
    }
}