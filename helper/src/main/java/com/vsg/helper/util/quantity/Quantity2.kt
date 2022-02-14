package com.vsg.helper.util.quantity

import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import com.vsg.helper.util.quantity.interfaces.IQty
import com.vsg.helper.util.unit.Unit
import com.vsg.helper.util.unit.type.TypeFormatUnit

class Quantity2: IQty {
    //region Events
    var eventAdvertenciaFueraLimites: (() -> String)? = null
    var eventAdvertenciaLimites: (()-> String)? = null
    //endregion

    //region Fields
    var  m_decimales:Int = 3
    var  m_dispose: Boolean = false
    var  m_quantity: Double = 0.0
    //endregion

    //region Properties
    override var quantity: Double = 0.0
        get() = this.Redondear()
        set(value)
        {
            var data: Double = value
            if (this.VerificarLimites && !value.getLimite(this.Minimo, this.Maximo)) { data = this.Minimo this.OnAdvertenciaFueraLimite() }
            field = value
        }
    
    var   Decimales: Int
        get() = field 
        set(value)
        {
            var data = value
            if (data > 15) data = 15
            if (data < 0) data = 0
            field = data
        }
    var Enteros: Int = 0
    val  Formato: String
        get()
        {
            var s: StringBuilder =  StringBuilder()
            try
            {
                when (this.TypeFormatUnit)
                {
                    DECIMALS -> s.append(0.toPadStart(this.Decimales))
                    INTEGERS -> repeat(this.Enteros){s.append("0")}

                }
            }
            catch
            {
                switch (this.TypeFormatUnit)
                {
                    case Util.Unidad.TypeFormatUnit.Decimales: s =  StringBuilder("0.000") break
                    case Util.Unidad.TypeFormatUnit.Enteros: s =  StringBuilder("0000") break
                }
            }
            return s.ToString()
        }
    var Unidad: Unit? = null
    val TypeFormatUnit: TypeFormatUnit
        get() = (this.unit == null) then TypeFormatUnit.UNDEFINED or unit!!.format

    val  ToCantidad: String  get() = String.format(this.format, this.quantity)
    val Absoluto: Double  get() = Math.abs(this.quantity)
    val ToAbsoluto: String get() { return this.Absoluto.ToString(this.Formato) } }

    public TypeValue TypeValue
    {
        get
        {
            if (this.m_quantity.Equals(0)) { return TypeValue.Cero }
            return this.m_quantity > 0
            ? TypeValue.Positivo
            : TypeValue.Negativo
        }
    }
    public ObjetoCantidad Invertir
    {
        get
        {
            switch (this.TypeValue)
            {
                case TypeValue.Negativo: return this.Positivo
                case TypeValue.Cero: return this
                case TypeValue.Positivo: return this.Negativo
            }
            return null
        }
    }
    public ObjetoCantidad Negativo
    {
        get
        {
            switch (this.TypeValue)
            {
                case TypeValue.Negativo: return this
                case TypeValue.Cero: return this
                case TypeValue.Positivo:
                ObjetoCantidad x = this.Clonado()
                x.quantity = this.Absoluto * -1
                return x
            }
            return null
        }
    }
    public ObjetoCantidad Positivo
    {
        get
        {
            switch (this.TypeValue)
            {
                case TypeValue.Negativo:
                ObjetoCantidad x = this.Clonado()
                x.quantity = this.Absoluto
                return x
                case TypeValue.Cero: return this
                case TypeValue.Positivo: return this
            }
            return null
        }
    }

    public Unidad.TypeUnidad TipoUnidad
    {
        get
        {
            TModel.TypeUnidad x = Util.Unidad.TypeUnidad.Adimensional
            try { x = (this.Unidad == null ? Util.Unidad.TypeUnidad.Adimensional : this.Unidad.Tipo) }
            catch { x = Util.Unidad.TypeUnidad.Adimensional }
            return x
        }
    }

    public Double ToDecimal { get { return this.quantity } }
    public double ToDouble { get { return Convert.ToDouble(this.quantity) } }
    public Int ToInt { get { return Convert.ToInt32(this.quantity) } }
    public long ToLong { get { return Convert.ToInt64(this.quantity) } }
    public ObjetoValor Valor { get { return  ObjetoValor(this.ToDecimal) } }

    public String Detalle
    {
        get
        {
            String x = String.Empty
            try { x = String.Format("{0} {1}", this.quantity, this.Unidad.Unidad.ToUpper()) }
            catch { x = "???" }
            return x
        }
    }

    //region Limits
    public Boolean VerificarLimites { get set }
    public Double Minimo { get private set }
    public Double Maximo { get private set }
    //endregion

    //endregion

    //region Methods

    //region Clon
    public ObjetoCantidad Clonado()
    {
        ObjetoCantidad p = null
        try
        {
            p =  ObjetoCantidad(this.TipoUnidad)
            {
                quantity = this.quantity,
                Decimales = this.Decimales,
                Enteros = this.Enteros
            }
            if (this.Unidad) { p.Unidad = this.Unidad.Clonado() }
        }
        catch { p = null }
        return p
    }
    //endregion

    public String CantidadDetallada()
    {
        String x = String.Empty
        try
        {
            x = String.Format("{0}: {1}", (this.Unidad != null ? this.Unidad.UnidadDetallada : "[]"), this.quantity.ToString(this.Formato))
        }
        catch { x = String.Empty }
        return x
    }
    public String CantidadDetallada(String detalles)
    {
        String x = String.Empty
        try { x = String.Format("{0}{1}", detalles, this.CantidadDetallada()) }
        catch { x = String.Empty }
        return x
    }
    public String CantidadAbsolutaDetallada()
    {
        String x = String.Empty
        try
        {
            x = String.Format("{0}: {1}", (this.Unidad != null ? this.Unidad.UnidadDetallada : "[]"), this.Absoluto.ToString(this.Formato))
        }
        catch { x = String.Empty }
        return x
    }
    public String CantidadAbsolutaDetallada(String detalles)
    {
        String x = String.Empty
        try { x = String.Format("{0}{1}", detalles, this.CantidadAbsolutaDetallada()) }
        catch { x = String.Empty }
        return x
    }

    private Double Redondear()
    {
        return this.Redondear(this.Decimales)
    }
    private Double Redondear(Int decimales)
    {
        Double x = 0
        try { x = Convert.ToDecimal(Math.Round(this.m_quantity, decimales)) }
        catch { x = this.m_quantity }
        return x
    }

    public void SetObjeto(Double cantidad)
    {
        try { this.quantity = cantidad }
        catch { }
    }
    public void SetObjeto(double cantidad, TModel.ObjetoUnidad unidad)
    {
        try
        {
            this.SetObjeto( ObjetoCantidad(cantidad, unidad))
        }
        catch { }
    }
    public void SetObjeto(ObjetoCantidad cantidad)
    {
        try
        {
            this.quantity = cantidad.quantity
            this.Decimales = cantidad.Decimales
            this.Enteros = cantidad.Enteros
            this.Unidad = cantidad.Unidad
        }
        catch { }
    }

    public String toString()
    {
        String x = String.Empty
        try { x = this.quantity.ToString(this.Formato) }
        catch { x = String.Empty }
        return x
    }

    //region Limits
    public void Limites(Double minimo, Double maximo)
    {
        this.OnAdvertenciaLimite(minimo, maximo)
        if (minimo >= maximo) { return }
        this.Minimo = minimo
        this.Maximo = maximo
        if (this.quantity.GetLimite(minimo, maximo)) { return }
        this.quantity = minimo
        this.OnAdvertenciaFueraLimite()
    }
    //endregion

    //region Overload
    public override String ToString() { return this.CantidadDetallada() }

    //region Operador
    public static ObjetoCantidad operator +(ObjetoCantidad e, ObjetoCantidad p)
    {
        ObjetoCantidad x = null
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return null }
            x =  ObjetoCantidad(e.quantity + p.quantity)
            if (e.Unidad != null && p.Unidad != null)
            {
                x.Unidad = ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
            }
        }
        catch { x = null }
        return x
    }
    public static ObjetoCantidad operator -(ObjetoCantidad e, ObjetoCantidad p)
    {
        ObjetoCantidad x = null
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return null }
            x =  ObjetoCantidad(e.quantity - p.quantity)
            if (e.Unidad != null && p.Unidad != null)
            {
                x.Unidad = ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
            }
        }
        catch { x = null }
        return x
    }
    public static ObjetoCantidad operator *(ObjetoCantidad e, ObjetoCantidad p)
    {
        ObjetoCantidad x = null
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return null }
            x =  ObjetoCantidad(e.quantity * p.quantity)
            if (e.Unidad != null && p.Unidad != null)
            {
                x.Unidad = ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
            }
        }
        catch { x = null }
        return x
    }
    public static ObjetoCantidad operator /(ObjetoCantidad e, ObjetoCantidad p)
    {
        ObjetoCantidad x = null
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return null }
            if (p.quantity.Equals(0)) { throw  DivideByZeroException(String.Format("{0} no puede ser cero...", p)) }
            x =  ObjetoCantidad(e.quantity / p.quantity)
            if (e.Unidad != null && p.Unidad != null)
            {
                x.Unidad = ((e.Unidad.Tipo == p.Unidad.Tipo) ? e.Unidad :  TModel.ObjetoUnidad(Util.Unidad.TypeUnidad.Adimensional))
            }
        }
        catch { x = null }
        return x
    }
    public static Boolean operator >(ObjetoCantidad e, ObjetoCantidad p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return false }
            x = (e.quantity > p.quantity)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator <(ObjetoCantidad e, ObjetoCantidad p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return false }
            x = (e.quantity < p.quantity)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator >(ObjetoCantidad e, Int p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (e.quantity > p)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator <(ObjetoCantidad e, Int p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (e.quantity < p)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator >(Double p, ObjetoCantidad e)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (p > e.ToDecimal)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator <(Double p, ObjetoCantidad e)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (p < e.ToDecimal)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator >=(ObjetoCantidad e, ObjetoCantidad p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return false }
            x = (e.quantity >= p.quantity)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator <=(ObjetoCantidad e, ObjetoCantidad p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return false }
            x = (e.quantity <= p.quantity)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator ==(ObjetoCantidad e, ObjetoCantidad p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return false }
            x = (e.quantity.Equals(p.quantity))
        }
        catch { x = false }
        return x
    }
    public static Boolean operator !=(ObjetoCantidad e, ObjetoCantidad p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null) || object.ReferenceEquals(p, null)) { return false }
            x = !(e.quantity.Equals(p.quantity))
        }
        catch { x = false }
        return x
    }

    public static Boolean operator >=(ObjetoCantidad e, double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (e.quantity >= Convert.ToDecimal(p))
        }
        catch { x = false }
        return x
    }
    public static Boolean operator <=(ObjetoCantidad e, double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (e.quantity <= Convert.ToDecimal(p))
        }
        catch { x = false }
        return x
    }
    public static Boolean operator ==(ObjetoCantidad e, double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (e.quantity.Equals(Convert.ToDecimal(p)))
        }
        catch { x = false }
        return x
    }
    public static Boolean operator !=(ObjetoCantidad e, double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = !(e.quantity.Equals(Convert.ToDecimal(p)))
        }
        catch { x = false }
        return x
    }

    public static Boolean operator >=(ObjetoCantidad e, Double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) return false
            x = (e.quantity >= p)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator <=(ObjetoCantidad e, Double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (e.quantity <= p)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator ==(ObjetoCantidad e, Double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (e.quantity.Equals(p))
        }
        catch { x = false }
        return x
    }
    public static Boolean operator !=(ObjetoCantidad e, Double p)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = !(e.quantity.Equals(p))
        }
        catch { x = false }
        return x
    }

    public static Boolean operator >=(Double p, ObjetoCantidad e)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (p >= e.ToDecimal)
        }
        catch { x = false }
        return x
    }
    public static Boolean operator <=(Double p, ObjetoCantidad e)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null)) { return false }
            x = (p <= e.ToDecimal)
        }
        catch { x = false }
        return x
    }

    public static Boolean operator true(ObjetoCantidad e)
    {
        Boolean x = false
        try
        {
            if (!object.ReferenceEquals(e, null) && e.Unidad != null && !e.quantity.Equals(0)) { x = true }
        }
        catch { }
        return x
    }
    public static Boolean operator false(ObjetoCantidad e)
    {
        Boolean x = false
        try
        {
            if (object.ReferenceEquals(e, null) || e.Unidad == null || e.quantity.Equals(0)) { x = true }
        }
        catch { }
        return x
    }

    public override Int GetHashCode() { return ToInt }
    public override Boolean Equals(object obj)
    {
        return obj is ObjetoCantidad && (obj as ObjetoCantidad) == this
    }
    //endregion

    //region Explicit
    public static explicit operator Boolean(ObjetoCantidad e)
    {
        return !object.ReferenceEquals(e, null) && Convert.ToBoolean(e.quantity)
    }
    public static explicit operator double(ObjetoCantidad e)
    {
        return object.ReferenceEquals(e, null) ? 0 : e.ToDouble
    }
    public static explicit operator Int(ObjetoCantidad e)
    {
        return object.ReferenceEquals(e, null) ? 0 : e.ToInt
    }
    public static explicit operator long(ObjetoCantidad e)
    {
        return object.ReferenceEquals(e, null) ? 0 : e.ToLong
    }
    public static explicit operator Double(ObjetoCantidad e)
    {
        return object.ReferenceEquals(e, null) ? 0 : e.quantity
    }

    public static explicit operator ObjetoCantidad(double e) { return  ObjetoCantidad(e) }
    public static explicit operator ObjetoCantidad(Double e) { return  ObjetoCantidad(e) }
    public static explicit operator ObjetoCantidad(Int e) { return  ObjetoCantidad(e) }
    public static explicit operator ObjetoCantidad(long e) { return  ObjetoCantidad(e) }
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
        if (d) { this.Unidad = null }
        this.m_dispose = true
    }
    //endregion

    //endregion

    //region Delegates
    private void OnAdvertenciaFueraLimite()
    {
        if (this.EventAdvertenciaFueraLimites == null || !this.VerificarLimites) { return }
        String c = String.Format("El valor {0} esta fuera de los límites establecido | límites: {1}-{2}",
        this.toString(),
        this.Minimo.ToString(this.Formato),
        this.Maximo.ToString(this.Formato))
        this.EventAdvertenciaFueraLimites(c)
    }
    private void OnAdvertenciaLimite(Double minimo, Double maximo)
    {
        if (this.EventAdvertenciaLimites == null || !this.VerificarLimites) { return }
        if (!(minimo >= maximo)) { return }
        StringBuilder sb =  StringBuilder()
        sb.AppendLine("El límite inferior debe ser superior al superior")
        sb.AppendLine(String.Format("Límite inferior: {0}", minimo.ToString(this.Formato)))
        sb.AppendLine(String.Format("Límite superior: {0}", maximo.ToString(this.Formato)))
        this.EventAdvertenciaLimites(sb.ToString())
    }
    //endregion

    //region Constructors
    public ObjetoCantidad()
    {
        this.Decimales = 3
        this.Enteros = 4
        this.Unidad =  TModel.ObjetoUnidad()
    }
    public ObjetoCantidad(double cantidad) : this() { this.quantity = Convert.ToDecimal(cantidad) }
    public ObjetoCantidad(double cantidad, String unidad) : this(cantidad) { this.Unidad =  TModel.ObjetoUnidad(unidad) }
    public ObjetoCantidad(double cantidad, TModel.TypeUnidad unidad) : this(cantidad) { this.Unidad =  TModel.ObjetoUnidad(unidad) }
    public ObjetoCantidad(double cantidad, Unidad.ObjetoUnidad unidad) : this(cantidad) { this.Unidad = unidad }
    public ObjetoCantidad(TModel.TypeUnidad unidad) : this() { this.Unidad =  TModel.ObjetoUnidad(unidad) }
    public ObjetoCantidad(TModel.ObjetoUnidad unidad) : this() { this.Unidad = unidad.Clonado() }
    public ObjetoCantidad(Int cantidad, TModel.TypeUnidad unidad) : this(Convert.ToDouble(cantidad), unidad) { }
    public ObjetoCantidad(Double cantidad) : this() { this.quantity = cantidad }
    public ObjetoCantidad(Double cantidad, TModel.TypeUnidad unidad) : this(cantidad) { this.Unidad =  TModel.ObjetoUnidad(unidad) }
    public ObjetoCantidad(Double cantidad, String unidad) : this(cantidad) { this.Unidad =  TModel.ObjetoUnidad(unidad) }
    public ObjetoCantidad(Double cantidad, TModel.ObjetoUnidad unidad) : this(cantidad) { this.Unidad = unidad }
    public ObjetoCantidad(Int cantidad) : this() { this.quantity = cantidad }
    public ObjetoCantidad(long cantidad) : this() { this.quantity = cantidad }
    //endregion

    //region Destructor
    ~ObjetoCantidad() { this.Dispose(false) }
    //endregion
}