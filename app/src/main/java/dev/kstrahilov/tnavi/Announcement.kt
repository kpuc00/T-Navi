package dev.kstrahilov.tnavi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Announcement(
    var title: String,
    var announcementFileName: String = "none"
) :
    Parcelable {
    override fun toString(): String {
        return title
    }
}