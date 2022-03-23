package com.vsg.helper.util.unit.type

import com.vsg.helper.ui.adapter.IDataAdapterDecimals
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.IDataAdapterSymbol
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart

enum class TypeUnit(
    override val value: Int,
    override val title: String,
    override val symbol: String = "",
    override val show: Boolean = true,
    override val default: Boolean = false,
    override val order: Int = -1,
    override var precision: Int = 3,
    override val isInt: Boolean = false,
    override val isException: Boolean = false,
    val typeFormatUnit: TypeFormatUnit = TypeFormatUnit.DECIMALS
) :
    IDataAdapterEnum, IDataAdapterSymbol, IDataAdapterDecimals {

    KG(value = 1, title = "Kilogramos", symbol = "kg", order = 1, default = true),
    PERCENT(value = 2, title = "Porcentaje", symbol = "%", order = 2),
    LITER(value = 3, title = "Litros", symbol = "lts", order = 3),
    UNDEFINED(
        value = -1,
        title = "Indefinido",
        show = false,
        isException = true,
        typeFormatUnit = TypeFormatUnit.UNDEFINED
    );

    companion object {
        fun TypeUnit.toFormat(value: Number): String =
            when (this.isInt) {
                true -> value.toPadStart(precision)
                false -> value.toFormat(precision)
            }

        fun TypeUnit.toUnitDetail(): String = "[${this.symbol}]"
    }
}
