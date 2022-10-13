package dev.kstrahilov.tnavi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Direction(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var route: ArrayList<Stop> = ArrayList(),
    var announcementFilePath: Int? = null
) :
    Parcelable {
    override fun toString(): String {
        return title
    }
}