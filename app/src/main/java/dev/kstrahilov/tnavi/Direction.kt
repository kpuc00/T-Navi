package dev.kstrahilov.tnavi

class Direction(var title: String, var route: ArrayList<Stop>) : java.io.Serializable {
    override fun toString(): String {
        return title
    }
}