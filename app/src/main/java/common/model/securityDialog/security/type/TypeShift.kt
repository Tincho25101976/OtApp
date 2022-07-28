package com.vsg.ot.common.model.securityDialog.security.type

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeShift(
    override val value: Int, override val title: String,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    MORNING_SHIFT(value = 1, title = "Mañana", order = 1, default = true),
    EVENING_SHIFT(value = 2, title = "Tarde", order = 2),
    NIGHT_SHIFT(value = 3, title = "Noche", order = 3),
    UNDEFINED(
        value = -1,
        title = "Indefinido",
        order = 1000,
        show = false,
        default = false,
        isException = true
    )
}