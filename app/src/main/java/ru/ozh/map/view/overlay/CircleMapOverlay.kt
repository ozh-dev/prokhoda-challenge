package ru.ozh.map.view.overlay

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import com.google.android.gms.maps.model.LatLng
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

class CircleMapOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MapOverlayLayout<PinView>(context, attrs) {

    var slideOffset = 0f
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

    private val originalCircle: Circle =
        Circle()
    private var tempCircle: Circle =
        Circle()

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

        context.withStyledAttributes(attrs, R.styleable.CircleMapOverlay) {
            val overLayColor = getColor(R.styleable.CircleMapOverlay_overlay_color, Color.BLACK)
            val clapX = getDimensionPixelOffset(R.styleable.CircleMapOverlay_overlay_clap_x, 0)
            val clapY = getDimensionPixelOffset(R.styleable.CircleMapOverlay_overlay_clap_y, 0)

            overlayPaint.color = overLayColor
            originalCircle.set(clapX.toFloat(), clapY.toFloat())
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val points = arrayOf(
            Point(300, 300),
            Point(400, 400),
            Point(500, 500),
            Point(600, 600)
        )

        for (point in points) {
            addMarker(buildPinView(point))
        }
    }

    private fun buildPinView(point: Point): PinView {
        return PinView(context, LatLng(.0, .0), point, R.drawable.ic_pin)
            .apply {
                layoutParams = LayoutParams(40.px, 40.px)
            }
    }

    /*override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPaint(overlayPaint)
        val (cX, cY) = anchorViewPoint
        if (slideOffset in 0f..1f) {
            val horizontalOffsetRatio = 1 - calculateProgressRatio(slideOffset, 0f, 1f)
            val horizontalOffsetAcceleratedRatio =
                accelerateInterpolator.getInterpolation(horizontalOffsetRatio)
            val horizontalOffsetDistance = ((width / 2f) - cX)
            horizontalOffset = horizontalOffsetDistance * horizontalOffsetAcceleratedRatio
        }

        val radiusRatio = 1 - calculateProgressRatio(slideOffset, -1f, 1f)
        val circleBoardY = height * radiusRatio

        if (circleBoardY < anchorViewPoint.y) {
            return
        }

        circleBoardPoint.set(cX, circleBoardY)
        val radius = calculateDistance(anchorViewPoint, circleBoardPoint) * 1.1f

        canvas.drawCircle(cX + horizontalOffset, cY, radius, holePaint)
    }*/

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPaint(overlayPaint)
        tempCircle.draw(canvas, circlePaint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateCirclePosition()
        invalidatePins()
    }

    private fun calculateCirclePosition() {
        val (x, y) = originalCircle.getCenter()

        //calculate circle x position
        if (halfOffsetRange.contains(slideOffset)) {
            val horizontalOffsetRatio = 1 - halfOffsetRange.progressRatio(slideOffset)
            val horizontalOffsetAcceleratedRatio =
                accelerateInterpolator.getInterpolation(horizontalOffsetRatio)
            val horizontalOffsetDistance = ((width / 2f) - x)
            horizontalOffset = horizontalOffsetDistance * horizontalOffsetAcceleratedRatio
        }

        //calculate circle radius
        val slideOffsetRatio = 1 - wholeOffsetRange.progressRatio(slideOffset)
        val radius = ((height * slideOffsetRatio) - y) * 1.1f

        tempCircle =
            originalCircle
                .setRadius(radius)
                .offset(dx = horizontalOffset)
    }

    private fun invalidatePins() {
        markersList.forEach { pinView ->
            if (!tempCircle.isBelong(pinView.point, radiusRatio = 0.7f)) {
                pinView.hide()
            } else {
                pinView.show()
            }
        }
    }
}