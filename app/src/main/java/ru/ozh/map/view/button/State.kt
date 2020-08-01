package ru.ozh.map.view.button

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import ru.ozh.map.R

sealed class State(
    @DrawableRes val iconDrawable: Int? = null,
    @ColorRes val colorBackground: Int = android.R.color.transparent
) {
    object Map : State(R.drawable.ic_map, R.color.area_black)
    object Close : State(R.drawable.ic_close, R.color.white)
}