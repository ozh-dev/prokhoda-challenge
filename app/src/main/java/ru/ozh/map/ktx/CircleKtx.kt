package ru.ozh.map.ktx

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.toPointF
import ru.ozh.map.`object`.Circle
import kotlin.math.pow
import kotlin.math.sqrt

object CircleKtx {

    fun Circle.offset(dx: Float = 0f, dy: Float = 0f): Circle {
        val (x, y) = getCenter()
        val radius = getRadius()
        return Circle(x + dx, y + dy, radius)
    }

    fun Circle.setRadius(radius: Float): Circle {
        val (x, y) = getCenter()
        return Circle(x, y, radius)
    }

    fun Circle.draw(canvas: Canvas, paint: Paint) {
        val (x, y) = getCenter()
        val radius = getRadius()
        canvas.drawCircle(x, y, radius, paint)
    }

    fun Circle.isBelong(point: Point): Boolean {
        val radius = getRadius()
        val centerPoint = getCenter()
        return calculateDistance(
            centerPoint,
            point.toPointF()
        ) <= radius
    }

    fun Circle.isBelong(point: Point, radiusRatio: Float = 1f): Boolean {
        val radius = getRadius()
        val centerPoint = getCenter()
        return calculateDistance(
            centerPoint,
            point.toPointF()
        ) <= radius * radiusRatio
    }

    fun calculateDistance(point1: PointF, point2: PointF): Float {
        val (x1, y1) = point1
        val (x2, y2) = point2
        return sqrt((x2 - x1).pow(2f) + (y2 - y1).pow(2f))
    }
}