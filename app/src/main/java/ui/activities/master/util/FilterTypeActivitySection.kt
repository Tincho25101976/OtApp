package com.vsg.ot.ui.activities.master.util

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class FilterTypeActivitySection(
    override val title: String, override val value: Int,
    override val order: Int = 1000, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    NAME(title = "Nombre", value = 1, order = 1, default = true),
    PREFIX(title = "Abreviatura", value = 2, order = 2),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true)
}