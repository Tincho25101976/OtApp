package com.vsg.helper.util.quantity

import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.util.helper.HelperNumeric.Companion.getLimite
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import com.vsg.helper.util.quantity.interfaces.IQty
import com.vsg.helper.util.quantity.type.TypeValue
import com.vsg.helper.util.quantity.type.TypeValue.*
import com.vsg.helper.util.unit.Unit
import com.vsg.helper.util.unit.type.TypeFormatUnit
import com.vsg.helper.util.unit.type.TypeUnit
import kotlin.math.abs

class Quantity : IQty {
    //region events
    var eventWarningOutLimit: ((String) -> kotlin.Unit)? = null
    //endregion

    override var quantity: Double = 0.0
        set(value) {
            if (this.checkLimit && value.getLimite(this.minimum, this.maximum)) {
                field = this.minimum

            }
        }

    var checkLimit: Boolean = false
    var minimum: Double = 0.0
        private set
    var maximum: Double = 0.0
        private set

    //region format
    var decimals: Int = 0
        set(value) {
            if (value > 15) field = 15
            if (value < 0) field = 0
            field = value
        }
    var integers: Int = 0
    val format: String
        get() {
            var s = StringBuilder()
            try {
                when (this.typeFormatUnit) {
                    TypeFormatUnit.DECIMALS -> s.append(0.toPadStart(decimals))
                    TypeFormatUnit.INTEGERS -> repeat(integers) { s.append("0") }
                    else -> {}
                }
            } catch (ex: Exception) {
                when (this.typeFormatUnit) {
                    TypeFormatUnit.DECIMALS -> s = StringBuilder("0.000")
                    TypeFormatUnit.INTEGERS -> s = StringBuilder("0000")
                    else -> {}
                }
            }
            return s.toString()
        }
    var unit: Unit? = null
    private val typeFormatUnit: TypeFormatUnit
        get() = this.unit?.format ?: TypeFormatUnit.UNDEFINED

    val toQuantity: String get() = String.format(format, quantity)
    val absolute: Double get() = abs(this.quantity)
    val toAbsolute: String get() = String.format(format, this.absolute)

    val typeValue: TypeValue
        get() {
            if (this.quantity.equals(0.0)) return CERO
            return (this.quantity > 0) then POSITIVO or NEGATIVO
        }
    val invert: Quantity
        get() =
            when (this.typeValue) {
                NEGATIVO -> this.negative
                CERO -> this
                POSITIVO -> this.positive
            }
    private val negative: Quantity
        get() = when (this.typeValue) {
            NEGATIVO -> this
            CERO -> this
            POSITIVO -> {
                val x: Quantity = this.clonado()
                x.quantity = this.absolute * -1
                x
            }
        }
    val positive: Quantity
        get() {
            return when (this.typeValue) {
                NEGATIVO -> {
                    val x: Quantity = this.clonado()
                    x.quantity = this.absolute
                    x
                }
                CERO -> this
                POSITIVO -> this
            }
        }

    val typeUnit: TypeUnit
        get() = (this.unit == null) then TypeUnit.UNDEFINED or unit!!.unit

    val toDouble: Double get() = this.quantity
    val toInt: Int get() = this.quantity.toInt()
    val toLong: Long get() = this.quantity.toLong()
    val toValue: QtyValue get() = QtyValue(this.quantity)

    val detalle: String
        get() = try {
            "${this.quantity} ${this.unit?.unit?.title}"
        } catch (ex: Exception) {
            "???"
        }
    //endregion

    //endregion

    //region methods
    fun toFormat(): String = String.format(this.format, this.quantity)

    //region clonado
    fun clonado(): Quantity {
        var p: Quantity
        try {
            Quantity(this.quantity, this.typeUnit).apply()
            {
                decimals = this@Quantity.decimals
                integers = this@Quantity.integers
            }.also { p = it }

        } catch (ex: Exception) {
            p = Quantity(); }
        return p
    }
    //endregion

    //endregion

    //region delegates
    private fun onWarningOutLimit() {
        if (!this.checkLimit) return
        val data = "El valor ${toString()} esta fuera de los límites establecido | límites: ${
            String.format(this.format, this.minimum)
        }-${String.format(this.format, this.maximum)}"
        this.eventWarningOutLimit?.invoke(data)
    }
    //endregion

    //region constructor
    constructor() {
        this.decimals = 3
        this.integers = 4
        this.unit = Unit(TypeUnit.UNDEFINED)
    }

    constructor(qty: Double) : this() {
        this.quantity = qty
    }

    constructor(qty: Int) : this(qty.toDouble())
    constructor(qty: Long) : this(qty.toDouble())
    constructor(qty: Double, unit: TypeUnit) : this(qty) {
        this.unit = Unit(unit)
    }

    constructor(qty: Int, unit: TypeUnit) : this(qty) {
        this.unit = Unit(unit)
    }

    constructor(qty: Long, unit: TypeUnit) : this(qty) {
        this.unit = Unit(unit)
    }

    constructor(unit: Unit) : this() {
        this.unit = unit
    }

    constructor(qty: Double, unit: Unit) : this(qty) {
        this.unit = unit
    }

    constructor(qty: Int, unit: Unit) : this(qty) {
        this.unit = unit
    }

    constructor(qty: Long, unit: Unit) : this(qty) {
        this.unit = unit
    }
    //endregion
}