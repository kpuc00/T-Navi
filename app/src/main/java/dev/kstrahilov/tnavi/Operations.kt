package dev.kstrahilov.tnavi

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Operations {
    private var gson = Gson()

    fun loadStopsFromInternalStorage(applicationContext: Context): ArrayList<Stop> {
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/stops.json"
        val file = File(path, fileName)

        return if (file.exists()) {
            val readJson = file.readText(Charsets.UTF_8)
            val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
            gson.fromJson(readJson, stopListType)
        } else {
            ArrayList()
        }
    }

    fun loadStopsJSONFromInternalStorage(applicationContext: Context): String {
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/stops.json"
        val file = File(path, fileName)

        return if (file.exists()) {
            file.readText(Charsets.UTF_8)
        } else {
            "[]"
        }
    }

    fun loadLinesFromInternalStorage(applicationContext: Context): ArrayList<Line> {
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/lines.json"
        val file = File(path, fileName)

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
        val fileName = "/lines.json"
        val file = File(path, fileName)
        val jsonString: String = gson.toJson(lines)
        file.writeText(jsonString, Charsets.UTF_8)
    }


    fun loadLinesJSONFromInternalStorage(applicationContext: Context): String {
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/lines.json"
        val file = File(path, fileName)

        return if (file.exists()) {
            file.readText(Charsets.UTF_8)
        } else {
            "[]"
        }
    }

    fun saveStopsToInternalStorageFromFile(applicationContext: Context, file: File): Boolean {
        val stopsData: String?
        val path: String = applicationContext.filesDir.toString()
        val fileName = "/stops.json"
        val internalFile = File(path, fileName)

        stopsData = file.readText(Charsets.UTF_8)
        val stopListType: Type = object : TypeToken<ArrayList<Stop?>?>() {}.type
        val readStops: ArrayList<Stop> = gson.fromJson(stopsData, stopListType)
        val jsonString: String = gson.toJson(readStops)
        internalFile.writeText(jsonString, Charsets.UTF_8)
        return true
    }

    fun exportDataToExternalStorage(
        applicationContext: Context, application: Application, json: String
    ): Boolean {
        val externalPath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .toString()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val externalFileName =
            application.getString(R.string.app_name) + "/data_" + LocalDateTime.now()
                .format(dateFormatter) + ".tnav"
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

    fun importFromExternalData(context: Context, uri: Uri): Boolean {
        val path = getRealPathFromURI(context, uri)
        return if (path != null) {
            val importedFile = File(path)
            val readFile = importedFile.readText(Charsets.UTF_8)
            Log.d("imported file content", readFile)
            true
        } else false
    }


    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/" + split[1]
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    }
                    isDownloadsDocument(uri) -> {
                        val fileName = getFilePath(context, uri)
                        if (fileName != null) {
                            return Environment.getExternalStorageDirectory()
                                .toString() + "/Download/" + fileName
                        }
                        var id = DocumentsContract.getDocumentId(uri)
                        if (id.startsWith("raw:")) {
                            id = id.replaceFirst("raw:".toRegex(), "")
                            val file = File(id)
                            if (file.exists()) return id
                        }
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
                        return getDataColumn(context, contentUri, null, null)
                    }
                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                }
            }
            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context, uri, null, null
                )
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    private fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(
                uri, projection, null, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}