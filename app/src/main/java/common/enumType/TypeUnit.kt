package common.model

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeUnit (
    override val value: Int, override val title: String, override val show: Boolean = true,
    override val default: Boolean = false, override val order: Int = -1, override val isException: Boolean = false
) :
    IDataAdapterEnum {
    CLIENT(value = 1, title = "Cliente", order = 3),
    PROVIDER(value = 2, title = "Proveedor", order = 2),
    USER(value = 3, title = "Usuario", default = true, order = 1),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true)
}