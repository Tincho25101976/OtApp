package common.model.std.common.type

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeEtapa(
    override val value: Int, override val title: String,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    PROCESO(value = 1, title = "Proceso", order = 1),
    FINAL(value = 2, title = "Final", order = 2, default = true),
    INTERMEDIOS(value = 3, title = "Intermedios", order = 3),
    MATERIA_PRIMA(value = 4, title = "Materia Prima", order = 4),
    DIARIO(value = 5, title = "Diario", order = 5),
    UNDEFINED(
        value = -1,
        title = "Indefinido",
        order = 1000,
        show = false,
        default = false,
        isException = true
    )
}