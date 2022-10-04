package dev.kstrahilov.tnavi

class Line(var number: String, var directions: ArrayList<Direction>) : java.io.Serializable {
    override fun toString(): String {
        return number
    }
}