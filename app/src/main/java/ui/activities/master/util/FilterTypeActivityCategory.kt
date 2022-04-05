package com.vsg.agendaandpublication.ui.activities.itemProducto.util

import com.vsg.utilities.ui.adapter.IDataAdapterEnum

enum class FilterTypeActivityCategory(
    override val value: Int,
    override val title: String,
    override val order: Int = 1000,
    override val show: Boolean = true,
    override val default: Boolean = false,
    override val isException: Boolean = false
) : IDataAdapterEnum {
    NAME(value = 1, title = "Categoría", order = 1, show = true, default = true),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true)
}
//enum class FilterTypeActivityCategory(val data: String) {
//    NAME("Categoría")
//}