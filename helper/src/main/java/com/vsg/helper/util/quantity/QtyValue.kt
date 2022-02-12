package com.vsg.helper.util.quantity

import com.vsg.helper.helper.Helper.Companion.toPadStart
import com.vsg.helper.helper.Helper.Companion.toRound
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.truncate

class QtyValue {
    //region properties
    var valor: Double = 0.0
        private set
    val absolute: Double get() = abs(valor)
    val integers: Double get() = truncate(valor)
    val decimals: Double get() = round(valor - integers).toRound(this.decimalPlace)
    var decimalPlace: Int = 3
        set(value) {
            var data = value
            if (value < 0) data = abs(value)
            field = data
        }
    val hasDecimals: Boolean get() = decimals > 0.0
    //endregion

    //region methods
    private fun genericFormat(format: String, value: Any): String = when (format.isEmpty()) {
        true -> "$value"
        else -> String.format(format, value)
    }

    public fun formatInteger(format: String): String = genericFormat(format, this.absolute)
    public fun formatDouble(format: String): String = genericFormat(format, decimals)
    private fun zero(len: Int): String = when (len <= 0) {
        true -> ""
        else -> 0.0.toPadStart(len)
    }

    public fun format(format: String): String = genericFormat(format, valor)
    //endregion
}