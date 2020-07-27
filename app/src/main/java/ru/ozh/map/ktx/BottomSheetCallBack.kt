package ru.ozh.map.ktx

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetCallBack(
    private val onSlide: ((bottomSheet: View, slideOffset: Float) -> Unit)? = null,
    private val onStateChanged: ((bottomSheet: View, newState: Int) -> Unit)? = null
) : BottomSheetBehavior.BottomSheetCallback() {
    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        onSlide?.invoke(bottomSheet, slideOffset)
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        onStateChanged?.invoke(bottomSheet, newState)
    }
}

fun BottomSheetBehavior<out View>.onSlide(onSlideCallback: ((bottomSheet: View, slideOffset: Float) -> Unit)) {
    this.addBottomSheetCallback(
        BottomSheetCallBack(
            onSlide = onSlideCallback
        )
    )
}

fun BottomSheetBehavior<out View>.onStateChanged(onStateChanged: (bottomSheet: View, newState: Int) -> Unit) {
    this.addBottomSheetCallback(
        BottomSheetCallBack(
            onStateChanged = onStateChanged
        )
    )
}