package com.vsg.helper.util.unit.type

import com.vsg.helper.ui.adapter.paging.IDataAdapterValue

enum class TypeFormatUnit(override val value: Int, override val title: String) :
    IDataAdapterValue {
    INTEGERS(value = 1, title = "Enteros"),
    DECIMALS(value = 2, title = "Decimales"),
    UNDEFINED(value = -1, title = "Indefinido")
}