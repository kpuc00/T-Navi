package dev.kstrahilov.tnavi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Line(
    val id: UUID = UUID.randomUUID(),
    var number: String,
    var directions: ArrayList<Direction>? = ArrayList(),
    var announcementFilePath: Int? = null
) : Parcelable {
    override fun toString(): String {
        return number
    }
}