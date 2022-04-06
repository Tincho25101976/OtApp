package com.vsg.ot.ui.activities.master.util

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class FilterTypeActivityProduct(
    override val title: String, override val value: Int,
    override val order: Int = 1000, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    NAME(value = 1, title = "Nombre del producto", order = 1, default = true),
    CODE(value = 2, title = "Código del producto", order = 3),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true);
}