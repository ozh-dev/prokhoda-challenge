package ru.ozh.map.ktx

import ru.ozh.map.`object`.FloatRange

object FloatRangeKtx {

    fun FloatRange.progressRatio(value: Float) : Float{
        return calculateProgressRatio(
            value,
            start,
            endInclusive
        )
    }

    fun calculateProgressRatio(
        slideOffset: Float,
        offsetMin: Float,
        offsetMax: Float
    ): Float {
        val result = ((slideOffset - offsetMin) * 100) / (offsetMax - offsetMin)
        return result / 100f
    }
}