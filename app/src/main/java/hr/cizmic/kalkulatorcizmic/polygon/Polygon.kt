package hr.cizmic.kalkulatorcizmic.polygon

import kotlin.math.abs

class Polygon(
    private var vertices: ArrayList<Vertex>
) {
    fun area(): Double {
        var area = 0.0
        var j = vertices.size - 1
        for (i in vertices.indices) {
            area += (vertices[j].x + vertices[i].x) * (vertices[j].y - vertices[i].y)
            j = i
        }
        return abs(area / 2.0)
    }
}