package ru.ozh.map.view.overlay

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import ru.ozh.map.Place
import ru.ozh.map.`object`.Circle
import ru.ozh.map.ktx.CircleKtx.draw
import ru.ozh.map.ktx.CircleKtx.isBelong
import ru.ozh.map.ktx.CircleKtx.offset
import ru.ozh.map.ktx.CircleKtx.setRadius
import ru.ozh.map.`object`.FloatRange
import ru.ozh.map.ktx.FloatRangeKtx.progressRatio
import ru.ozh.map.R
import ru.ozh.map.ktx.px
import ru.ozh.map.view.base.MapOverlayLayout

/**
 * Draw pins and circle above map
 */
class MapCircleOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MapOverlayLayout<PinView>(context, attrs) {

    var slideOffset = 1f
        set(value) {
            field = value
            calculateCirclePosition()
            invalidatePins()
            invalidate()
        }

    private val halfOffsetRange = FloatRange(0f, 1f)
    private val wholeOffsetRange = FloatRange(-1f, 1f)
    private var horizontalOffset = 0f
    private val accelerateInterpolator = AccelerateInterpolator(1.2f)

    private val originalCircle: Circle = Circle()
    private var tempCircle: Circle = Circle()

    private val circlePaint = Paint()
        .apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

    private val overlayPaint = Paint()
        .apply {
            isAntiAlias = true
        }

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        context.withStyledAttributes(attrs, R.styleable.MapCircleOverlay) {
            val overLayColor = getColor(R.styleable.MapCircleOverlay_overlay_color, Color.BLACK)
            val clapX = getDimensionPixelOffset(R.styleable.MapCircleOverlay_overlay_clap_x, 0)
            val clapY = getDimensionPixelOffset(R.styleable.MapCircleOverlay_overlay_clap_y, 0)

            overlayPaint.color = overLayColor
            originalCircle.set(clapX.toFloat(), clapY.toFloat())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPaint(overlayPaint)
        tempCircle.draw(canvas, circlePaint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateCirclePosition()
    }

    fun setPlaces(places: List<Place>, map: GoogleMap) {
        places.forEach { place ->
            addMarker(
                buildPinView(
                    place.placeLatLng,
                    map.projection.toScreenLocation(place.placeLatLng),
                    place.isOpen
                )
            )
        }
    }

    private fun buildPinView(latLng: LatLng, point: Point, isEnable: Boolean): PinView {
        return PinView(
            context,
            latLng,
            point,
            R.drawable.ic_pin,
            context.getColor(if (isEnable) R.color.swatch_5 else R.color.red)
        )
            .apply { layoutParams = LayoutParams(40.px, 40.px) }
    }

    private fun calculateCirclePosition() {
        val (x, y) = originalCircle.getCenter()

        //calculate circle x offset position
        if (halfOffsetRange.contains(slideOffset)) {
            val horizontalOffsetRatio = 1 - halfOffsetRange.progressRatio(slideOffset)
            val horizontalOffsetAcceleratedRatio =
                accelerateInterpolator.getInterpolation(horizontalOffsetRatio)
            val horizontalOffsetDistance = ((width / 2f) - x)
            horizontalOffset = horizontalOffsetDistance * horizontalOffsetAcceleratedRatio
        }

        //calculate circle radius
        val slideOffsetRatio = 1 - wholeOffsetRange.progressRatio(slideOffset)
        val radius = ((height * slideOffsetRatio) - y) * 1.15f

        //draw circle
        tempCircle =
            originalCircle
                .setRadius(radius)
                .offset(dx = horizontalOffset)
    }

    //draw pins that belongs to tempCircleRadius
    private fun invalidatePins() {
        markersList.forEach { pinView ->
            if (tempCircle.isBelong(pinView.point, radiusRatio = 0.7f)) {
                pinView.show()
            } else {
                pinView.hide()
            }
        }
    }
}