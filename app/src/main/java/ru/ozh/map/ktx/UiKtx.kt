package ru.ozh.map.ktx

import android.content.res.Resources
import android.graphics.Rect
import android.view.View

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val View.screenLocation
    get(): IntArray {
        val point = IntArray(2)
        getLocationInWindow(point)
        return point
    }

val View.bounds
    get(): Rect {
        val (x, y) = screenLocation
        return Rect(x, y, x + width, y + height)
    }