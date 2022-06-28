package com.vsg.helper.common.export

import android.graphics.Bitmap
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.helper.array.HelperArray.Companion.toBitmap
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import com.vsg.helper.helper.string.HelperString.Static.toBool
import java.util.*
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

class ExportGenericEntityItem<T>(
    val name: String,
    val allowNull: Boolean = false,
    val value: T? = null,
    var nameReport: String = ""
) {
    val type: KType?
        get() {
            if (value == null) return null
            return value!!::class.starProjectedType
        }
    val isByteArray: Boolean get() = type == ByteArray::class.starProjectedType
    val isItemReport: Boolean get() = nameReport.isNotEmpty() && isTypeForDataStringReport
    private val isTypeForDataStringReport: Boolean
        get() {
            return when (type) {
                Int::class.starProjectedType -> true
                Double::class.starProjectedType -> true
                String::class.starProjectedType -> true
                Long::class.starProjectedType -> true
                Date::class.starProjectedType -> true
                Boolean::class.starProjectedType -> true
                else -> false
            }
        }
    val valueCast: String
        get() {
            val data = (value == null) then "" or value.toString()
            return when (type) {
                Int::class.starProjectedType -> data.toInt().toString()
                Double::class.starProjectedType -> data.toDouble().toString()
                String::class.starProjectedType -> data
                Long::class.starProjectedType -> data.toLong().toString()
                Date::class.starProjectedType -> (value as Date?).toDateString(FormatDateString.SIMPLE)
                Boolean::class.starProjectedType -> data.toBool().toString()
                else -> data
            }
        }
    val valueBitmap: Bitmap?
        get() {
            if (valueByteArray == null || valueByteArray!!.isEmpty()) return null
            return valueByteArray!!.toBitmap()
        }
    val valueByteArray: ByteArray?
        get() {
            if (!isByteArray) return null
            return value as ByteArray
        }
}