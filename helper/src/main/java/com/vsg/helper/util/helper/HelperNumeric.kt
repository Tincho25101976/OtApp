package com.vsg.helper.util.helper

import androidx.annotation.IntRange
import java.text.DecimalFormat
import kotlin.math.round

class HelperNumeric {
    companion object {
        const val TO_PIXEL: Double = 0.264583333

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

        public fun Number.getMayor(superior: Number, defecto: Number): Number =
            try {
                when (this => superior) {
                    true -> defecto
                    else -> this
                }
            } catch (ex: Exception) {
                this
            }

        public fun Number.getMenor(inferior: Number, defecto: Number): Number =
            try {
                when (this <= inferior) {
                    true -> defecto
                    else -> this
                }
            } catch (ex: Exception) {
                this
            }

        public fun Number.getLimite(minimo: Number, maximo: Number): Boolean =
            this in minimo..maximo

        public fun Number.getLimite(minimo: Number, maximo: Number, defecto: Number): Number =
            try {
                when (this.getLimite(minimo, maximo)) {
                    true -> this
                    else -> defecto
                }
            } catch (ex: Exception) {
                this
            }

        public fun Number.getLimite( inferior:Number, superior:Number,  positivo:Number,  negativo:Number):Number =
            try {
                when (this.getMayor(superior, positivo).equals(this){ true -> this.getMenor(inferior, negativo) else -> valor}
            } catch(ex: Exception) {this}
    }
}