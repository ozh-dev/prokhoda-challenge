package ru.ozh.map.view.overlay

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.view.ViewGroup
import android.view.animation.*
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.animation.addListener
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.LatLng
import ru.ozh.map.ktx.px
import ru.ozh.map.view.base.MarkerView

class PinView(
    context: Context,
    latLng: LatLng,
    point: Point,
    @DrawableRes val icon: Int
) : MarkerView(context, latLng, point) {

    private val iconBtm =
        AppCompatResources.getDrawable(context, icon)
            ?.toBitmap(40.px, 40.px)

    private val hideAnimator by lazy {
        buildObjectAnimator(
            interpolator = AccelerateInterpolator(),
            from = 1f,
            to = 0f,
            onStart = {
                hadShown = false
            }
        )
    }

    private val showAnimator by lazy {
        buildObjectAnimator(
            interpolator = OvershootInterpolator(),
            from = 0f,
            to = 1f,
            onStart = {
                hadShown = true
            }
        )
    }

    private val iconPaint = Paint()
        .apply {
            isAntiAlias = true
        }

    private var hadShown = true

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        iconBtm?.let {
            canvas.drawBitmap(it, 0f, 0f, iconPaint)
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        val frameParams = params as FrameLayout.LayoutParams
        frameParams.leftMargin = point.x - frameParams.width / 2
        frameParams.topMargin = point.y - frameParams.height
        pivotX = frameParams.width / 2f
        pivotY = frameParams.height.toFloat()
        super.setLayoutParams(frameParams)
    }

    override fun show() {
        if (!hadShown) {
            showAnimator.start()
        }
    }

    override fun hide() {
        if (hadShown) {
            hideAnimator.start()
        }
    }

    private fun buildObjectAnimator(
        interpolator: BaseInterpolator,
        from: Float,
        to: Float,
        onStart: (Animator) -> Unit = {}
    ): ValueAnimator {
        return ObjectAnimator.ofFloat(from, to)
            .apply {
                this.duration = 250
                this.interpolator = interpolator
                addUpdateListener {
                    val value = it.animatedValue as Float
                    this@PinView.scaleX = value
                    this@PinView.scaleY = value
                }
                addListener(onStart = onStart)
            }
    }
}