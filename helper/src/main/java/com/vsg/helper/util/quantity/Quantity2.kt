package com.vsg.helper.util.quantity

import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import com.vsg.helper.util.quantity.interfaces.IQty
import com.vsg.helper.util.quantity.type.TypeValue
import com.vsg.helper.util.unit.Unit
import com.vsg.helper.util.unit.type.TypeFormatUnit

class Quantity2 : IQty {
    //region Events
    var eventAdvertenciaFueraLimites: (() -> String)? = null
    var eventAdvertenciaLimites: (() -> String)? = null
    //endregion

    //region Fields
    var m_decimales: Int = 3
    var m_dispose: Boolean = false
    var m_quantity: Double = 0.0
    //endregion

    //region Properties
    override var quantity: Double = 0.0
        get() = this.Redondear()
        set(value) {
            var data: Double = value
            if (this.VerificarLimites && !value.getLimite(this.Minimo, this.Maximo)) {
                data = this.Minimo this.OnAdvertenciaFueraLimite()
            }
            field = value
        }

    var Decimales: Int
        get() = field
        set(value) {
            var data = value
            if (data > 15) data = 15
            if (data < 0) data = 0
            field = data
        }
    var Enteros: Int = 0
    val Formato: String
        get() {
            var s: StringBuilder = StringBuilder()
            try {
                when (this.TypeFormatUnit) {

                    TypeFormatUnit.DECIMALS -> s.append(0.toPadStart(this.Decimales))
                    TypeFormatUnit.INTEGERS -> repeat(this.Enteros) { s.append("0") }
                    else -> {}
                }
            } catch (ex: Exception) {
                when (this.TypeFormatUnit) {

                    Util.Unidad.TypeFormatUnit.Decimales: s = StringBuilder("0.000")
                    break
                            Util . Unidad . TypeFormatUnit . Enteros : s = StringBuilder ("0000") break
                }
            }
            return s.toString()
        }
    var Unidad: Unit? = null
    val TypeFormatUnit: TypeFormatUnit
        get() = (this.Unidad == null) then TypeFormatUnit.UNDEFINED or Unidad!!.format

    val ToCantidad: String get() = String.format(this.Formato, this.quantity)
    val Absoluto: Double get() = Math.abs(this.quantity)
    val ToAbsoluto: String
        get() {
            return this.Absoluto.ToString(this.Formato)
        }
}

val TypeValue: TypeValue
    get() {
        if (this.quantity.Equals(0.0)) {
            return TypeValue.Cero
        }
        return this.quantity > 0.0
        ? TypeValue.Positivo
        : TypeValue.Negativo
    }
}
val Invertir: Quantity2
    get() {
        when (this.TypeValue) {
            TypeValue.Negativo: return this.Positivo
            TypeValue.Cero: return this
            TypeValue.Positivo: return this.Negativo
        }
        return null
    }
val Negativo: Quantity2
    get() {
        when (this.TypeValue) {
            TypeValue.Negativo: return this
            TypeValue.Cero: return this
            TypeValue.Positivo:
                val x
                : Quantity2 = this.Clonado()
            x.quantity = this.Absoluto * -1
                return x
        }
        return null
    }
val Positivo: Quantity2?
    get() {
        when (this.TypeValue) {
            TypeValue.Negativo:
                val x
                : Quantity2 = this.Clonado()
            x.quantity = this.Absoluto
                return x
            TypeValue.Cero: return this
            TypeValue.Positivo: return this
        }
        return null
    }

val TipoUnidad: TypeUnidad
    get() {
        val x: TypeUnidad = Util.Unidad.TypeUnidad.Adimensional
        try {
            x = (this.Unidad == null ? Util.Unidad.TypeUnidad.Adimensional : this.Unidad.Tipo)
        } catch {
            x = Util.Unidad.TypeUnidad.Adimensional
        }
        return x
    }

val ToDouble: Double get() = this.quantity
val ToInt: Int get() = this.quantity.ToInt()
val ToLong: Long get() = this.quantity.toLong()
val Valor: QtyValue get() = QtyValue(this.quantity)

val Detalle: String
    get() = "${this.quantity} ${this.Unidad.title}"

//region Limits
var VerificarLimites: Boolean = false
var Minimo: Double = 0.0
var Maximo: Double = 0.0
//endregion

//endregion

//region Methods

//region Clon
fun Clonado(): Quantity2? {
    var p: Quantity2? = null
    try {
        p = Quantity2(this.TipoUnidad).apply
        {
            quantity = this.quantity
            Decimales = this.Decimales
            Enteros = this.Enteros
        }
        if (this.Unidad) {
            p.Unidad = this.Unidad.Clonado()
        }
    } catch (ex: Exception) {
        p = null
    }
    return p
}
//endregion

fun symbol(): String = (this.Unidad == null) then "[]" or this.Unidad.symbol
fun CantidadDetallada(): String = "${symbol()}: ${this.ToCantidad}"

fun CantidadDetallada(detalles: String): String = "${detalles}${this.CantidadDetallada()}"

fun CantidadAbsolutaDetallada(): String =
    "${symbol()}: ${String.format(this.Formato, this.Absoluto)}"

fun  CantidadAbsolutaDetallada (detalles: String ): String
{
    String x = String . Empty
            try {
                x = String.Format("${detalles}{1}", detalles, this.CantidadAbsolutaDetallada())
            } catch {
                x = String.Empty
            }
    return x
}

private Double Redondear ()
{
    return this.Redondear(this.Decimales)
}
private Double Redondear (Int decimales)
{
    Double x = 0
    try {
        x = Convert.ToDecimal(Math.Round(this.m_quantity, decimales))
    } catch {
        x = this.m_quantity
    }
    return x
}

public void SetObjeto (Double cantidad)
{
    try {
        this.quantity = cantidad
    } catch {
    }
}
public void SetObjeto (double cantidad, TModel.ObjetoUnidad unidad)
{
    try {
        this.SetObjeto(Quantity2(cantidad, unidad))
    } catch {
    }
}
public void SetObjeto(Quantity2 cantidad)
{
    try {
        this.quantity = cantidad.quantity
        this.Decimales = cantidad.Decimales
        this.Enteros = cantidad.Enteros
        this.Unidad = cantidad.Unidad
    } catch {
    }
}

public String toString()
{
    String x = String . Empty
            try {
                x = this.quantity.ToString(this.Formato)
            } catch {
                x = String.Empty
            }
    return x
}

//region Limits
public void Limites(Double minimo, Double maximo)
{
    this.OnAdvertenciaLimite(minimo, maximo)
    if (minimo >= maximo) {
        return
    }
    this.Minimo = minimo
    this.Maximo = maximo
    if (this.quantity.GetLimite(minimo, maximo)) {
        return
    }
    this.quantity = minimo
    this.OnAdvertenciaFueraLimite()
}
//endregion

//region Overload
public override String ToString() { return this.CantidadDetallada() }

//region Operador
public static Quantity2 operator +(Quantity2 e, Quantity2 p)
{
    Quantity2 x = null
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return null
        }
        x = Quantity2(e.quantity + p.quantity)
        if (e.Unidad != null && p.Unidad != null) {
            x.Unidad =
                ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
        }
    } catch {
        x = null
    }
    return x
}
public static Quantity2 operator -(Quantity2 e, Quantity2 p)
{
    Quantity2 x = null
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return null
        }
        x = Quantity2(e.quantity - p.quantity)
        if (e.Unidad != null && p.Unidad != null) {
            x.Unidad =
                ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
        }
    } catch {
        x = null
    }
    return x
}
public static Quantity2 operator *(Quantity2 e, Quantity2 p)
{
    Quantity2 x = null
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return null
        }
        x = Quantity2(e.quantity * p.quantity)
        if (e.Unidad != null && p.Unidad != null) {
            x.Unidad =
                ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
        }
    } catch {
        x = null
    }
    return x
}
public static Quantity2 operator /(Quantity2 e, Quantity2 p)
{
    Quantity2 x = null
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return null
        }
        if (p.quantity.Equals(0)) {
            throw  DivideByZeroException(String.Format("{0} no puede ser cero...", p))
        }
        x = Quantity2(e.quantity / p.quantity)
        if (e.Unidad != null && p.Unidad != null) {
            x.Unidad =
                ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
        }
    } catch {
        x = null
    }
    return x
}
public static Boolean operator >(Quantity2 e, Quantity2 p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return false
        }
        x = (e.quantity > p.quantity)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator <(Quantity2 e, Quantity2 p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return false
        }
        x = (e.quantity < p.quantity)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator >(Quantity2 e, Int p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (e.quantity > p)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator <(Quantity2 e, Int p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (e.quantity < p)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator >(Double p, Quantity2 e)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (p > e.ToDecimal)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator <(Double p, Quantity2 e)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (p < e.ToDecimal)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator >=(Quantity2 e, Quantity2 p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return false
        }
        x = (e.quantity >= p.quantity)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator <=(Quantity2 e, Quantity2 p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return false
        }
        x = (e.quantity <= p.quantity)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator ==(Quantity2 e, Quantity2 p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return false
        }
        x = (e.quantity.Equals(p.quantity))
    } catch {
        x = false
    }
    return x
}
public static Boolean operator !=(Quantity2 e, Quantity2 p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) {
            return false
        }
        x = !(e.quantity.Equals(p.quantity))
    } catch {
        x = false
    }
    return x
}

public static Boolean operator >=(Quantity2 e, double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (e.quantity >= Convert.ToDecimal(p))
    } catch {
        x = false
    }
    return x
}
public static Boolean operator <=(Quantity2 e, double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (e.quantity <= Convert.ToDecimal(p))
    } catch {
        x = false
    }
    return x
}
public static Boolean operator ==(Quantity2 e, double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (e.quantity.Equals(Convert.ToDecimal(p)))
    } catch {
        x = false
    }
    return x
}
public static Boolean operator !=(Quantity2 e, double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = !(e.quantity.Equals(Convert.ToDecimal(p)))
    } catch {
        x = false
    }
    return x
}

public static Boolean operator >=(Quantity2 e, Double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) return false
        x = (e.quantity >= p)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator <=(Quantity2 e, Double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (e.quantity <= p)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator ==(Quantity2 e, Double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (e.quantity.Equals(p))
    } catch {
        x = false
    }
    return x
}
public static Boolean operator !=(Quantity2 e, Double p)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = !(e.quantity.Equals(p))
    } catch {
        x = false
    }
    return x
}

public static Boolean operator >=(Double p, Quantity2 e)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (p >= e.ToDecimal)
    } catch {
        x = false
    }
    return x
}
public static Boolean operator <=(Double p, Quantity2 e)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null)) {
            return false
        }
        x = (p <= e.ToDecimal)
    } catch {
        x = false
    }
    return x
}

public static Boolean operator true(Quantity2 e)
{
    Boolean x = false
    try {
        if (!object.ReferenceEquals(e, null) && e.Unidad != null && !e.quantity.Equals(0)) {
            x = true
        }
    } catch {
    }
    return x
}
public static Boolean operator false(Quantity2 e)
{
    Boolean x = false
    try {
        if (object.ReferenceEquals(e, null) || e.Unidad == null || e.quantity.Equals(0)) {
            x = true
        }
    } catch {
    }
    return x
}

public override Int GetHashCode() { return ToInt }
public override Boolean Equals(object obj)
{
    return obj is Quantity2 && (obj as Quantity2) == this
}
//endregion

//region Explicit
public static explicit operator Boolean(Quantity2 e)
{
    return !object.ReferenceEquals(e, null) && Convert.ToBoolean(e.quantity)
}
public static explicit operator double(Quantity2 e)
{
    return object.ReferenceEquals(e, null) ? 0 : e.ToDouble
}
public static explicit operator Int(Quantity2 e)
{
    return object.ReferenceEquals(e, null) ? 0 : e.ToInt
}
public static explicit operator long(Quantity2 e)
{
    return object.ReferenceEquals(e, null) ? 0 : e.ToLong
}
public static explicit operator Double(Quantity2 e)
{
    return object.ReferenceEquals(e, null) ? 0 : e.quantity
}

public static explicit operator Quantity2(double e) { return Quantity2(e) }
public static explicit operator Quantity2(Double e) { return Quantity2(e) }
public static explicit operator Quantity2(Int e) { return Quantity2(e) }
public static explicit operator Quantity2(long e) { return Quantity2(e) }
//endregion

//endregion

//region Dispose
public void Dispose()
{
    this.Dispose(true)
    GC.SuppressFinalize(this)
}
private void Dispose(Boolean d)
{
    if (this.m_dispose) return
    if (d) {
        this.Unidad = null
    }
    this.m_dispose = true
}
//endregion

//endregion

//region Delegates
private void OnAdvertenciaFueraLimite()
{
    if (this.EventAdvertenciaFueraLimites == null || !this.VerificarLimites) {
        return
    }
    String c = String . Format ("El valor {0} esta fuera de los límites establecido | límites: {1}-{2}",
    this.toString(),
    this.Minimo.ToString(this.Formato),
    this.Maximo.ToString(this.Formato))
    this.EventAdvertenciaFueraLimites(c)
}
private void OnAdvertenciaLimite(Double minimo, Double maximo)
{
    if (this.EventAdvertenciaLimites == null || !this.VerificarLimites) {
        return
    }
    if (!(minimo >= maximo)) {
        return
    }
    StringBuilder sb = StringBuilder ()
    sb.AppendLine("El límite inferior debe ser superior al superior")
    sb.AppendLine(String.Format("Límite inferior: {0}", minimo.ToString(this.Formato)))
    sb.AppendLine(String.Format("Límite superior: {0}", maximo.ToString(this.Formato)))
    this.EventAdvertenciaLimites(sb.ToString())
}
//endregion

//region Constructors
public Quantity2()
{
    this.Decimales = 3
    this.Enteros = 4
    this.Unidad = TModel.ObjetoUnidad()
}
public Quantity2(double cantidad) : this() { this.quantity = Convert.ToDecimal(cantidad) }
public Quantity2(double cantidad, String unidad) : this(cantidad) {
    this.Unidad = TModel.ObjetoUnidad(unidad)
}
public Quantity2(double cantidad, TModel.TypeUnidad unidad) : this(cantidad) {
    this.Unidad = TModel.ObjetoUnidad(unidad)
}
public Quantity2(double cantidad, Unidad.ObjetoUnidad unidad) : this(cantidad) {
    this.Unidad = unidad
}
public Quantity2(TModel.TypeUnidad unidad) : this() { this.Unidad = TModel.ObjetoUnidad(unidad) }
public Quantity2(TModel.ObjetoUnidad unidad) : this() { this.Unidad = unidad.Clonado() }
public Quantity2(Int cantidad, TModel.TypeUnidad unidad) : this(Convert.ToDouble(cantidad), unidad) {}
public Quantity2(Double cantidad) : this() { this.quantity = cantidad }
public Quantity2(Double cantidad, TModel.TypeUnidad unidad) : this(cantidad) {
    this.Unidad = TModel.ObjetoUnidad(unidad)
}
public Quantity2(Double cantidad, String unidad) : this(cantidad) {
    this.Unidad = TModel.ObjetoUnidad(unidad)
}
public Quantity2(Double cantidad, TModel.ObjetoUnidad unidad) : this(cantidad) {
    this.Unidad = unidad
}
public Quantity2(Int cantidad) : this() { this.quantity = cantidad }
public Quantity2(long cantidad) : this() { this.quantity = cantidad }
//endregion

//region Destructor
~Quantity2() { this.Dispose(false) }
//endregion
}