package common.model.master.item.type

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypePlant(
    override val value: Int, override val title: String,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    BURZACO_QUIMICOS(value = 1, title = "Químicos (Burzaco)", order = 1, default = true),
    BURZACO_BLADDERS(value = 2, title = "Bladders (Burzaco)", order = 2),
    SAN_LUIS_QUIMICOS(value = 3, title = "Químicos (San Luis)", order = 3),
    UNDEFINED(
        value = -1,
        title = "Indefinido",
        order = 1000,
        show = false,
        default = false,
        isException = true
    )
}