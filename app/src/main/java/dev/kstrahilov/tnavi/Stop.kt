package dev.kstrahilov.tnavi

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Stop(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var location: LatLng = LatLng(0.0, 0.0),
    var announcementFilePath: Int? = null,
    var isCurrent: Boolean = false,
    var isNext: Boolean = false,
    var isAnnounced: Boolean = false,
) : Parcelable {
    override fun toString(): String {
        return title
    }
}