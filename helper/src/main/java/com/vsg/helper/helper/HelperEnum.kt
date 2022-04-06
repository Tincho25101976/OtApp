package com.vsg.helper.helper

import com.vsg.helper.helper.array.HelperArray.Companion.toRandom
import com.vsg.helper.ui.adapter.IDataAdapterEnum

class HelperEnum {
    companion object {
        class EnumIterator<T : Enum<T>>(type: Class<T>) : Iterator<T?> {
            private var cursor = 0

            // your original code gets them on each hasNext/next call
            private val values: Array<T>? = type.enumConstants
            fun toList(): List<T> = when (values == null) {
                true -> listOf()
                false -> values.toList()
            }

            override fun hasNext(): Boolean = when (values == null) {
                true -> false
                false -> cursor < values.size - 1
            }

            override fun next(): T? = when (values == null) {
                true -> null
                false -> values[cursor++]
            }
        }

        fun <T : Enum<T>> toRandom(type: Class<T>, defaultValue: T): T {
            val data = EnumIterator(type).toList()
            return data.toRandom() ?: defaultValue
        }
        fun <T : Enum<T>> Class<T>.toRandomEnum(defaultValue: T): T {
            val data = EnumIterator(this).toList()
            return data.toRandom() ?: defaultValue
        }

        fun <T> getList(type: Class<T>): List<T> where T : IDataAdapterEnum, T : Enum<T> {
            val default = EnumIterator(type).toList().firstOrNull { it.show && it.default }
            val temp: List<T> = when (default != null) {
                true -> EnumIterator(type).toList().filter { it.show && it.value != default.value }
                false -> EnumIterator(type).toList().filter { it.show }
            }
            val data: MutableList<T> = mutableListOf()
            if (default != null) data.add(default)
            data.addAll(temp.sortedBy { it.order })
            return data
        }

        fun <T> getDefault(type: Class<T>): T where T : IDataAdapterEnum, T : Enum<T> {
            val temp: T? = EnumIterator(type).toList().firstOrNull { it.show && it.default }
            return when (temp == null) {
                true -> EnumIterator(type).toList().minByOrNull { it.order }!!
                false -> temp
            }
        }

        fun <T> getIsException(type: Class<T>): T where T : IDataAdapterEnum, T : Enum<T> {
            var temp: T? = EnumIterator(type).toList().firstOrNull { it.isException }
            if (temp != null) return temp
            temp = EnumIterator(type).toList().firstOrNull { it.name == "UNDEFINED" }
            if (temp != null) return temp
            temp = EnumIterator(type).toList().firstOrNull { !it.show && !it.default }
            if (temp != null) return temp
            return EnumIterator(type).toList().maxByOrNull { it.order }!!
        }
    }
}