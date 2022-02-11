package com.vsg.helper.util.quantity.type

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeValue(
    override val value: Int, override val title: String,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    NEGATIVO(value = -1, title = "Negativo", order = 1),
    CERO(value = 0, title = "Cero", order = 2, default = true),
    POSITIVO(value = 1, title = "Positivo", order = 3)
    }