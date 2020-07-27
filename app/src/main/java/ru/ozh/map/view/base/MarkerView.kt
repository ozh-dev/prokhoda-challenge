package ru.ozh.map.view.base

import android.content.Context
import android.graphics.Point
import android.view.View
import androidx.annotation.CallSuper
import com.google.android.gms.maps.model.LatLng

/**
 * Абстракная view для создания маркеров на [MapOverlayLayout]
 */
abstract class MarkerView private constructor(context: Context) : View(context) {
    lateinit var point: Point
        private set
    lateinit var latLng: LatLng
        private set

    protected val latitude: Double
        get() = latLng.latitude

    protected val longitude: Double
        get() = latLng.longitude

    constructor(context: Context, latLng: LatLng, point: Point) : this(context) {
        this.latLng = latLng
        this.point = point
    }

    abstract fun show()

    abstract fun hide()

    @CallSuper
    open fun refresh(point: Point) {
        this.point = point
    }
}