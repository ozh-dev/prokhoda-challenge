package ru.ozh.map.view.base

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.ArrayList

/**
 * Абстракный контейнер для создания Overlay для карты
 */
open class MapOverlayLayout<V : MarkerView?> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val currentLatLng: LatLng
        get() = LatLng(
            googleMap?.cameraPosition?.target?.latitude ?: 0.0,
            googleMap?.cameraPosition?.target?.longitude ?: 0.0
        )

    protected val markersList: MutableList<V> = ArrayList()

    private var googleMap: GoogleMap? = null

    protected fun addMarker(view: V) {
        markersList.add(view)
        addView(view)
    }

    protected fun removeMarker(view: V) {
        markersList.remove(view)
        removeView(view)
    }

    protected fun getProjection(latLng: LatLng): Point {
        return googleMap?.projection?.toScreenLocation(latLng) ?: Point()
    }

    fun setOnCameraIdleListener(listener: GoogleMap.OnCameraIdleListener?) {
        googleMap?.setOnCameraIdleListener(listener)
    }

    fun setOnCameraMoveListener(listener: GoogleMap.OnCameraMoveListener) {
        googleMap?.setOnCameraMoveListener(listener)
    }

    fun showAllMarkers() {
        for (i in markersList.indices) {
            markersList[i]?.show()
        }
    }

    fun hideAllMarkers() {
        for (i in markersList.indices) {
            markersList[i]?.hide()
        }
    }

    fun showMarker(position: Int) {
        markersList[position]?.show()
    }

    fun setupMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    fun getGoogleMap(): GoogleMap? {
        return googleMap
    }

    fun moveCamera(latLngBounds: LatLngBounds) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150))
    }

    fun moveCamera(latLng: LatLng, zoom: Float) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun scrollCamera(dx: Float = 0f, dy: Float = 0f) {
        googleMap?.moveCamera(CameraUpdateFactory.scrollBy(dx, dy))
    }

    fun moveCamera(cameraUpdateFactory: CameraUpdate) {
        googleMap?.moveCamera(cameraUpdateFactory)
    }

    fun refresh() {
        for (i in markersList.indices) {
            markersList[i]?.latLng?.let { latLng ->
                refresh(i, getProjection(latLng))
            }
        }
    }

    private fun refresh(position: Int, point: Point) {
        markersList[position]?.refresh(point)
    }
}
