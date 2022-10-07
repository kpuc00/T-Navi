package dev.kstrahilov.tnavi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Direction(var title: String, var route: ArrayList<Stop>, var announcementFilePath: Int?) :
    Parcelable {
    override fun toString(): String {
        return title
    }
}