package com.vsg.agendaandpublication.ui.activities.itemOperation.util

import com.vsg.utilities.ui.adapter.IDataAdapterEnum

enum class FilterTypeActivityBatch(
    override val title: String, override val value: Int,
    override val order: Int = 1000, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    VALUE_CODE(title = "Batch", value = 1, order = 1, default = true),
    PRODUCT(title = "Producto", value = 2, order = 2),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true)
}