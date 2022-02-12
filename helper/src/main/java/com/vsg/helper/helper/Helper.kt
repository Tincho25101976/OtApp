package com.vsg.helper.helper

import androidx.annotation.IntRange
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.date.HelperDate.Companion.now
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import java.text.DecimalFormat
import java.util.*
import kotlin.math.round

class Helper {
    companion object {

        //region boolean
        fun Boolean.toSiNo(): String = when (this) {
            true -> "Si"
            false -> "No"
        }

        fun Boolean.toActivo(): String = when (this) {
            true -> "Activo"
            false -> "Inactivo"
        }

        fun Boolean.toCount(): Int = when (this) {
            true -> 1
            false -> 0
        }
        //endregion

        //region long
        fun Long?.toDate(): Date? {
            return when (this == null) {
                true -> null
                false -> Date(this)
            }
        }
        //endregion

        //region file
        fun fileName(type: Class<*>, extension: String): String {
            if (extension.isEmpty()) return ""
            return type.simpleName + '_' + now().toDateString(FormatDateString.FILE) + '.' + extension
        }
        //endregion

        //region operation
        fun getOperationByInt(value: Int): DBOperation {
            val e = DBOperation.values().firstOrNull { it.value == value }
            return when (e == null) {
                true -> DBOperation.INDEFINIDO
                false -> e
            }
        }

        fun getOperationByData(value: String): DBOperation {
            val e = DBOperation.values().firstOrNull { it.data == value }
            return when (e == null) {
                true -> DBOperation.INDEFINIDO
                false -> e
            }
        }
        //endregion

        //region reflection
//        fun kTypeOf(type: KClass<*>): KType = type.createType()
//        inline fun <reified T : Any> kTypeOf(): KType {
//            val type: KType = T::class.createType()
//            return type
//        }
        //endregion
    }
}