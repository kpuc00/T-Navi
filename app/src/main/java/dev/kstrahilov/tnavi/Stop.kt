package dev.kstrahilov.tnavi

import android.location.Location


class Stop(
    var title: String,
    var isCurrent: Boolean,
    var isNext: Boolean,
    var location: Location?,
    var announcementFilePath: String?
) : java.io.Serializable {
    override fun toString(): String {
        return title
    }
}