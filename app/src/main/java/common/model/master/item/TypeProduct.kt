package common.model.master.item

import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeProduct(
    override val value: Int, override val title: String,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) : IDataAdapterEnum {
    RAW_MATERIAL(value = 1, title = "Materia Prima", order = 1),
    PROCESS_PRODUCT(value = 2, title = "Intermedio", order = 2, default = true),
    PROCESS_PRODUCT_BULK(value = 3, title = "Intermedio (Granel)", order = 3),
    FINISH_PRODUCT(value = 4, title = "Final", order = 4),
    RESALE(value = 5, title = "Reventa", order = 5),
    UNDEFINED(
        value = -1,
        title = "Indefinido",
        order = 1000,
        show = false,
        default = false,
        isException = true
    )
}