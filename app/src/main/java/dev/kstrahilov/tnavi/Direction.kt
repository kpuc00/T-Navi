package dev.kstrahilov.tnavi

class Direction(var title: String, var route: ArrayList<Stop>) {
    override fun toString(): String {
        return title
    }
}