package ru.ozh.map.view.button

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import ru.ozh.map.ktx.px
import kotlin.math.max
import kotlin.math.min

class CircleButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private val minSize = 40.px
    private var animationProgressRatio = 1f

    private val progressAnimator = ObjectAnimator.ofFloat(0f, 1f)
        .apply {
            duration = 200
            addUpdateListener {
                val value = it.animatedValue as Float
                animationProgressRatio = value
                invalidate()
            }
        }

    private val paintBackground = Paint()
        .apply {
            isAntiAlias = true
            color = Color.TRANSPARENT
        }

    private val paintBitmap = Paint()
        .apply {
            isAntiAlias = true
        }

    private var iconBtm: Bitmap? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val (cX, cY) = (width / 2f) to (height / 2f)
        val radius = width / 2f
        paintBackground.alpha = (animationProgressRatio * 255f).toInt()
        canvas.drawCircle(cX, cY, radius, paintBackground)

        val btnWidth = iconBtm?.width ?: 0
        val btnHeight = iconBtm?.height ?: 0
        val left = (width - btnWidth) / 2f
        val top = (height - btnHeight) / 2f

        canvas.save()
        canvas.scale(animationProgressRatio, animationProgressRatio, cX, cY)
        iconBtm?.let { btm ->
            canvas.drawBitmap(btm, left, top, paintBitmap)
        }
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMode)

        val desireWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> width
            MeasureSpec.AT_MOST -> min(minSize, width)
            else -> minSize
        }

        val desireHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> height
            MeasureSpec.AT_MOST -> min(minSize, height)
            else -> minSize
        }

        val size = max(desireHeight, desireWidth)

        setMeasuredDimension(size, size)
    }

    fun show(state: State) {
        translateTo(state) {
            progressAnimator.start()
        }
    }

    fun hide() {
        progressAnimator.reverse()
    }

    private fun translateTo(state: State, action: () -> Unit) {
        if(progressAnimator.isRunning) return
        paintBackground.color = context.getColor(state.colorBackground)
        state.iconDrawable?.let {
            iconBtm = context.getDrawable(state.iconDrawable)?.toBitmap(24.px, 24.px)
        }
        action()
    }
}