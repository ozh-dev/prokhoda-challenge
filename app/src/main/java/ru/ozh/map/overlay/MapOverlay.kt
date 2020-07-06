package ru.ozh.map.overlay

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import ru.ozh.map.R
import kotlin.math.pow
import kotlin.math.sqrt

class MapOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    var slideOffset = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val anchorViewPoint = PointF(0f, 0f)
    private val circleBoardPoint = PointF(0f, 0f)
    private val anchorViewRect = Rect()
    private var horizontalOffset = 0f
    private val accelerateInterpolator = AccelerateInterpolator(1.2f)

    private val holePaint = Paint()
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

        context.withStyledAttributes(attrs, R.styleable.MapOverlay) {
            val overLayColor = getColor(R.styleable.MapOverlay_overlay_color, Color.BLACK)
            overlayPaint.color = overLayColor
        }
    }

    override fun onDraw(canvas: Canvas) {
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
    }

//    override fun dispatchDraw(canvas: Canvas?) {
//        super.dispatchDraw(canvas)
//    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val anchorView = this.findViewWithTag<View>(context.getString(R.string.overlay_btn_tag))
        anchorView.getHitRect(anchorViewRect)
        anchorViewPoint.set(anchorViewRect.centerX().toFloat(), anchorViewRect.centerY().toFloat())
    }

    private fun calculateDistance(point1: PointF, point2: PointF): Float {
        val (x1, y1) = point1
        val (x2, y2) = point2
        return sqrt((x2 - x1).pow(2f) + (y2 - y1).pow(2f))
    }

    private fun calculateProgressRatio(
        slideOffset: Float,
        offsetMin: Float,
        offsetMax: Float
    ): Float {
        val result = ((slideOffset - offsetMin) * 100) / (offsetMax - offsetMin)
        return result / 100f
    }
}