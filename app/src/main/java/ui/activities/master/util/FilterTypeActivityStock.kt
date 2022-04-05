package com.vsg.agendaandpublication.ui.activities.itemOperation.util

import com.vsg.utilities.ui.adapter.IDataAdapterEnum

enum class FilterTypeActivityStock(
    override val title: String, override val value: Int,
    override val order: Int = 1000, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    PRODUCT_NAME(title = "Nombre del producto", value = 1, order = 1, default = true),
    PRODUCT_VALUE_CODE(title = "Código del producto", value = 1, order = 1, default = true),
    BATCH(title = "Lote", value = 2, order = 2, default = true),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true)
}