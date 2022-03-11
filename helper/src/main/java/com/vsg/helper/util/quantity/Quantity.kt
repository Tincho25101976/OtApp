package com.vsg.helper.util.quantity

import android.R.string
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
    var eventAdvertenciaFueraLimites: (() -> String)? = null
    //endregion

    override var quantity: Double
        get() = TODO("Not yet implemented")
        set(value) {
            var data = value
            if (this.verificarLimites && value.getLimite(this.minimo, this.maximo)) {
                field = this.minimo

            }
        }

    var verificarLimites: Boolean = false
    var minimo: Double = 0.0
        private set(value) {
            field = value
        }
    var maximo: Double = 0.0
        private set(value) {
            field = value
        }

    //region format
    var decimals: Int = 0
        get() = field
        set(value) {
            if (value > 15) field = 15
            if (value < 0) field = 0
            field = value;
        }
    var integers: Int = 0
    val format: String
        get() {
            var s = StringBuilder()
            try {
                when (this.typeFormatUnidad) {
                    TypeFormatUnit.DECIMALS -> s.append(0.toPadStart(decimals))
                    TypeFormatUnit.INTEGERS -> repeat(integers) { _ -> s.append("0") }
                    else -> {}
                }
            } catch (ex: Exception) {
                when (this.typeFormatUnidad) {
                    TypeFormatUnit.DECIMALS -> s = StringBuilder("0.000")
                    TypeFormatUnit.INTEGERS -> s = StringBuilder("0000")
                    else -> {}
                }
            }
            return s.toString()
        }
    var unit: Unit? = null
    private val typeFormatUnidad: TypeFormatUnit
        get() = this.unit?.format ?: TypeFormatUnit.UNDEFINED

    public val toCantidad: String get() = String.format(format, quantity)
    public val absolute: Double get() = abs(this.quantity)
    public val toAbsolute: String get() = String.format(format, this.absolute)

    val typeValue: TypeValue
        get() {
            if (this.quantity.equals(0.0)) return CERO
            return (this.quantity > 0) then POSITIVO or NEGATIVO
        }
    val invertir: Quantity
        get() {
            when (this.typeValue) {
                NEGATIVO -> this.negativo
                CERO -> this
                POSITIVO -> this.Positivo
            }
        }
    private val negativo: Quantity
        get() {
            when (this.typeValue) {
                NEGATIVO -> return this
                CERO -> return this
                POSITIVO -> {
                    val x: Quantity = this.clonado();
                    x.quantity = this.absolute * -1;
                    return x;
                }
            }
        }
    public val Positivo: Quantity
        get() {
            return when (this.typeValue) {
                NEGATIVO -> {
                    val x: Quantity = this.clonado()
                    x.quantity = this.absolute;
                    x;
                }
                CERO -> this;
                POSITIVO -> this;
            }
        }

    val tipoUnidad: TypeUnit
        get() = (this.unit == null) then TypeUnit.UNDEFINED or unit!!.unit

    val toDouble: Double get() = this.quantity
    val toInt: Int get() = this.quantity.toInt()
    val toLong: Long get() = this.quantity.toLong()
    val valor: QtyValue get() = QtyValue(this.quantity)

    public val Detalle: String
        get() {
            var x: String = ""
            try {
                x = "${this.quantity} ${this.unit?.unit?.title}"
            } catch (ex: Exception) {
                x = "???"; }
            return x;
        }
    //endregion

    //endregion

    //region methods
    public fun toFormat(): String = String.format(this.formato, this.quantity)

    //region clonado
    fun clonado(): Quantity {
        val p: Quantity(this.).app = null;
        try {
            p = new ObjetoCantidad (this.TipoUnidad)
            {
                Cantidad = this.Cantidad,
                Decimales = this.Decimales,
                Enteros = this.Enteros
            };
            if (this.Unidad) {
                p.Unidad = this.Unidad.Clonado(); }
        } catch {
            p = null; }
        return p;
    }
    //endregion

//endregion

    //region delegates
    private fun onAdvertenciaFueraLimites() {
        if (!this.verificarLimites) return
        val c: string = string.Format(
            "El valor ${} esta fuera de los límites establecido | límites: {1}-{2}",
            this.toString(),
            this.minimo.ToString(this.Formato),
            this.Maximo.ToString(this.Formato)
        )
        this.EventAdvertenciaFueraLimites(c)
    }
//endregion
}