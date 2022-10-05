package dev.kstrahilov.tnavi

class Line(
    var number: String,
    var directions: ArrayList<Direction>,
    var announcementFilePath: Int?
) : java.io.Serializable {
    override fun toString(): String {
        return number
    }
}