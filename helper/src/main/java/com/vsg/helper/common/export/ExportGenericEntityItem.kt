package com.vsg.helper.common.export

import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.helper.date.HelperDate.Companion.toDate
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
    val isItemReport: Boolean get() = nameReport.isNotEmpty()
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
}