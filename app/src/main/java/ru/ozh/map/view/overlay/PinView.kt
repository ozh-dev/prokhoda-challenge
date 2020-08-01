package ru.ozh.map.view.overlay

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BaseInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.animation.addListener
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.updateLayoutParams
import com.google.android.gms.maps.model.LatLng
import ru.ozh.map.ktx.px
import ru.ozh.map.view.base.MarkerView

@SuppressLint("ViewConstructor")
class PinView(
    context: Context,
    latLng: LatLng,
    point: Point,
    @DrawableRes val icon: Int,
    @ColorInt color: Int
) : MarkerView(context, latLng, point) {

    private val iconBtm =
        AppCompatResources.getDrawable(context, icon)
            ?.apply { setTint(color) }
            ?.toBitmap(40.px, 40.px)

    private val hideAnimator by lazy {
        buildObjectAnimator(
            interpolator = AccelerateInterpolator(),
            from = 1f,
            to = 0f,
            duration = 150,
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

    private var hadShown = false

    init {
        //default state
        scaleX = 0f
        scaleY = 0f
    }

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

    override fun refresh(point: Point) {
        super.refresh(point)
        updateLayoutParams(point)
    }

    private fun updateLayoutParams(point: Point) {
        updateLayoutParams<FrameLayout.LayoutParams> {
            marginStart = point.x - width / 2
            topMargin = point.y - height / 2
        }
        invalidate()
    }

    private fun buildObjectAnimator(
        interpolator: BaseInterpolator,
        from: Float,
        to: Float,
        duration: Long = 250,
        onStart: (Animator) -> Unit = {}
    ): ValueAnimator {
        return ObjectAnimator.ofFloat(from, to)
            .apply {
                this.duration = duration
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