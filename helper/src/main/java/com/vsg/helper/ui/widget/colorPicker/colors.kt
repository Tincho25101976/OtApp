package com.vsg.helper.ui.widget.colorPicker

import android.graphics.Color.*
import androidx.annotation.FloatRange
import androidx.annotation.NonNull
import kotlin.math.roundToLong

fun interColor(@FloatRange(from = 0.0, to = 1.0) unit: Float, @NonNull colors: IntArray): Int {
    if (unit <= 0) return colors[0]
    if (unit >= 1) return colors[colors.size - 1]

    var p = unit * (colors.size - 1)
    val i = p.toInt()
    // take fractional part
    p -= i

    val c0 = colors[i]
    val c1 = colors[i + 1]
    // Calculates each channel separately
    val a: Int = avg(alpha(c0), alpha(c1), p).toInt()
    val r: Int = avg(red(c0), red(c1), p).toInt()
    val g: Int = avg(green(c0), green(c1), p).toInt()
    val b: Int = avg(blue(c0), blue(c1), p).toInt()

    return argb(a, r, g, b)
}

/**
 * Calculates int value in between two integers using percentage
 * @param s - start
 * @param e - end
 * @param p - scaled percentage
 */
fun avg(s: Int, e: Int, @FloatRange(from = 0.0, to = 1.0) p: Float) =
    s + (p * (e - s)).roundToLong()