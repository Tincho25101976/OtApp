package com.vsg.agendaandpublication.ui.activities.itemProducto.util

import com.vsg.utilities.ui.adapter.IDataAdapterEnum

enum class FilterTypeActivityProduct(
    override val title: String, override val value: Int,
    override val order: Int = 1000, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    NAME(value = 1, title = "Nombre del producto", order = 1, default = true),
    CODE(value = 2, title = "Código del producto", order = 3),
    PROVIDER_CODE(value = 3, title = "Código del proveedor", order = 3),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true);
}