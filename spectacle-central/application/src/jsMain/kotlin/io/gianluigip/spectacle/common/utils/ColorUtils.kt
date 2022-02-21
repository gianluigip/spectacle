package io.gianluigip.spectacle.common.utils

import csstype.Color

/**
 * Change the saturation of a Hex color
 */
fun String.lighter(percentage: Int): Color {
    if (percentage > 100 || percentage < 0) throw IllegalArgumentException("The percentage has to be between 0 and 100 rather it was $percentage")
    if (!this.toString().startsWith("#")) throw IllegalArgumentException("The color has to be an hex number but it was $this")

    return Color(toString() + percentageToAlpha(percentage))

}

private fun percentageToAlpha(percentage: Int): String {
    return ((percentage * 250) / 100).toString(16)
}