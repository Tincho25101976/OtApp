package common.model.master.section.type

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeSection(
    override val value: Int, override val title: String,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    CONTROL(value = 1, title = "Control", order = 1),
    RESTRICTED(value = 2, title = "Restringido", order = 2),
    NORMAL(value = 3, title = "Normal", order = 3, default = true),
    RESERVED(value = 4, title = "Reservado", order = 4),
    UNDEFINED(
        value = -1,
        title = "Indefinido",
        order = 1000,
        show = false,
        default = false,
        isException = true
    )
}