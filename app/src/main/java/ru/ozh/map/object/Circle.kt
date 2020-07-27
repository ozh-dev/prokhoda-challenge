package ru.ozh.map.`object`

import android.graphics.PointF

class Circle(
    private var x: Float = 0f,
    private var y: Float = 0f,
    private var r: Float = 0f
) {

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

//    fun set(radius: Float) {
//        this.r = radius
//    }

    fun getRadius(): Float {
        return r
    }

    fun getCenter(): PointF {
        return PointF(x, y)
    }
}