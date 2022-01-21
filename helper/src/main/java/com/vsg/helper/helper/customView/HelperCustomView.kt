package com.vsg.helper.helper.customView

import android.content.res.TypedArray
import androidx.core.content.res.getFloatOrThrow
import com.vsg.helper.common.utilEnum.IValue
import com.vsg.helper.helper.HelperEnum
import com.vsg.helper.helper.date.HelperDate.Companion.toDate
import java.util.*

class HelperCustomView {
    companion object {
        //region attribute
        inline fun <reified T> TypedArray.getEnum(index: Int, default: T, type: Class<T>): T
                where T : Enum<T>, T : IValue {
            //getInt(index, -1).let { if (it >= 0) enumValues<T>()[it] else default }
            val data = getInt(index, -1)
            val result: T =
                if (data >= 0) {
                    HelperEnum.Companion.EnumIterator(type).toList()
                        .firstOrNull { it.value == data } ?: default
                } else {
                    default
                }
            return result
        }

        fun TypedArray.getDate(index: Int): Date? =
            getString(index).let { it?.toDate() }

        fun TypedArray.getStringOrDefault(index: Int, default: String = "") =
            getString(index).let { if (it.isNullOrEmpty()) default else it }

        fun TypedArray.getFloatOrDefault(index: Int, default: Float? = null): Float? =
            try {
                getFloatOrThrow(index)
            } catch (e: Exception) {
                default
            }

        fun TypedArray.getDoubleOrDefault(index: Int, default: Double? = null): Double? {
            val data = this.getFloatOrDefault(index)
            return when (data == null) {
                true -> default
                false -> data.toDouble()
            }
        }
        //endregion
    }
}