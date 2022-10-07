package dev.kstrahilov.tnavi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Line(
    var number: String, var directions: ArrayList<Direction>, var announcementFilePath: Int?
) : Parcelable {
    override fun toString(): String {
        return number
    }
}