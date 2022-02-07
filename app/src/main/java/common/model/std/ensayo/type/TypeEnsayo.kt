package common.model.std.ensayo.type

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeEnsayo(
    override val value: Int, override val title: String,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    CUALITATIVO(value = 1, title = "Cuantitativo", order = 1),
    CUANTITATIVO(value = 2, title = "Cualitativo", order = 2, default = true),
    UNDEFINED(
        value = -1,
        title = "Indefinido",
        order = 1000,
        show = false,
        default = false,
        isException = true
    )
}