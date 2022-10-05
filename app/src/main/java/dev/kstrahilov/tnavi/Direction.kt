package dev.kstrahilov.tnavi

class Direction(var title: String, var route: ArrayList<Stop>, var announcementFilePath: Int?) :
    java.io.Serializable {
    override fun toString(): String {
        return title
    }
}