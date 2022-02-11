package com.vsg.helper.util.quantity

import com.vsg.helper.helper.Helper.Companion.toPadStart
import com.vsg.helper.helper.Helper.Companion.toRound
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.truncate

class QtyValue {
    //region properties
    var valor: Double = 0.0
        private set(value) {}
    val absolute: Double get() = abs(valor)
    val integers: Double get() = truncate(valor)
    val decimals: Double get() = round(valor - integers).toRound(this.decimalPlace)
    var decimalPlace: Int = 3
        set(value) {
            var data = value
            if (value < 0) data = abs(value)
            field = data
        }
    val isDecimals: Boolean get() = decimals > 0.0
    //endregion

    //region methods
    private fun genericFormat(formato: String, value: Any): String {
        return when (formato.isEmpty()) {
            true -> "$value"
            else -> String.format(formato, value)
        }
    }

    public fun FormatoEntero(formato: String): String = genericFormat(formato, this.absolute)
    public fun FormatoDecimal(formato: String): String = genericFormat(formato, decimals)
    private fun Ceros(largo: Int): String {
        return when (largo <= 0) {
            true -> ""
            else -> 0.0.toPadStart(largo)
        }
    }

    public fun Formato(int enteros, int decimales, SeparadorDecimal separador): String {
        if (enteros < 0 || decimales < 0) return ""
        val sb: StringBuilder = StringBuilder()
        val x: String = ""
        try {
            // parte entera:
            String f_entero = GetFormatoString (enteros, new String(ENTERO, enteros))
            String cadena_entero = String . Format (f_entero, this.Enteros)
            String cadena_entero_final = String . IsNullOrWhiteSpace (cadena_entero)
            ? String.Empty
            : cadena_entero.Length > enteros
            ? cadena_entero.Substring(0, enteros)
            : cadena_entero
            sb.Append(cadena_entero_final)

            if (this.PoseeDecimales) {
                if (this.PoseeSeparadorDecimales) {
                    switch(separador)
                    {
                        case SeparadorDecimal . Punto :
                        sb.Append(PUNTO)
                        break
                        case SeparadorDecimal . Coma :
                        sb.Append(COMA)
                        break
                        case SeparadorDecimal . No :
                        break
                        default:
                        throw new ArgumentOutOfRangeException ("separador")
                    }
                }

                // parte decimal:
                String f_decimal = new String(ENTERO, decimales)
                f_decimal = GetFormatoString(decimales, new String (ENTERO, decimales))

                double factor_multiplicar = decimales > 1
                ? (Math.Pow(10, (decimales - 1)))
                : 1
                double parte_decimal = this.GetParteDecimal(this.Decimales)

                double parteDecimal = parte_decimal * factor_multiplicar
                        String cadena_decimal = String . Format (f_decimal, parteDecimal)
                String cadena_decimal_final = String . IsNullOrWhiteSpace (cadena_decimal)
                ? String.Empty
                : cadena_decimal.Length > decimales
                ? cadena_decimal.Substring(0, decimales)
                : cadena_decimal

                sb.Append(cadena_decimal_final)
            } else {
                sb.Append(new String (ENTERO, decimales))
            }
            x = sb.ToString()
            sb = null
        } catch {
        }
        return x
    }

    public fun Formato(formato: String): String = genericFormat(formato, valor)

    private double GetParteDecimal(decimal e)
    {
        double x = 0
        try {
            String v = e . ToString (CultureInfo.InvariantCulture)
            if (String.IsNullOrWhiteSpace(v)) {
                return 0
            }
            if (v.Contains(this.Separador)) {
                String g = v . Substring (v.IndexOf(
                    this.Separador,
                    System.StringComparison.Ordinal
                ) + 1)
                if (!String.IsNullOrWhiteSpace(g)) {
                    x = double.Parse(g)
                }
            }
        } catch {
            x = 0
        }
        return x
    }
    //endregion

    companion object {
        private fun  GetFormatoString( largo: Int,  formato: String): String
        {
            return String.format("{0}0,{1}:{2}{3}", CORCHETE_ABRIR, largo, formato, CORCHETE_CERRAR)
        }
        private static String GetFormatoString(int largo, char formato)
        {
            return String.Format("{0}0,{1}:{2}{3}", CORCHETE_ABRIR, largo, formato, CORCHETE_CERRAR)
        }
    }
}