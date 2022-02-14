package com.vsg.helper.util.helper

import androidx.annotation.IntRange
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import java.text.DecimalFormat
import kotlin.math.round

class HelperNumeric {
    companion object {
        private const val TO_PIXEL: Double = 0.264583333

        fun Double.toRound(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return round(this * multiplier) / multiplier
        }

        fun Number.toFormat(decimalPlace: Int = 2): String {
            val df = DecimalFormat()
            df.maximumFractionDigits = decimalPlace
            df.minimumFractionDigits = decimalPlace
            return df.format(this)
        }

        fun Number.toPadStart(@IntRange(from = 2, to = 10) length: Int = 4): String =
            this.toString().padStart(length, '0')

        public fun Double.getMayor(superior: Double, defecto: Double): Double =
            try {
                (this >= superior) then defecto or this
            } catch (ex: Exception) {
                this
            }

        public fun Double.getMenor(inferior: Double, defecto: Double): Double =
            try {
                (this <= inferior) then defecto or this
            } catch (ex: Exception) {
                this
            }

        public fun Double.getLimite(minimo: Double, maximo: Double): Boolean =
            this in minimo..maximo

        public fun Double.getLimite(minimo: Double, maximo: Double, defecto: Double): Double =
            try {
                this.getLimite(minimo, maximo) then this or defecto
            } catch (ex: Exception) {
                this
            }

        public fun Double.getLimite(
            inferior: Double,
            superior: Double,
            positivo: Double,
            negativo: Double
        ): Double =
            try {
                this.getMayor(superior, positivo).equals(this) then this.getMenor(
                    inferior,
                    negativo
                ) or this
            } catch (ex: Exception) {
                this
            }

        public fun Int.milimeterToPixel(): Int = (this / TO_PIXEL).toInt()
        public fun Int.pixelToMilimeter(): Int = (this * TO_PIXEL).toInt()
    }
}