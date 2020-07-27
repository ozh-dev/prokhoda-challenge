package ru.ozh.map.`object`

class FloatRange(
    override val start: Float,
    override val endInclusive: Float
) : ClosedFloatingPointRange<Float> {

    override fun contains(value: Float): Boolean = value in start..endInclusive

    override fun lessThanOrEquals(a: Float, b: Float): Boolean = a <= b
}