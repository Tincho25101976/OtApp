package common.enumType

import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.adapter.IDataAdapterSymbol

enum class TypeUnit (
    override val value: Int,
    override val title: String,
    override val symbol: String = "",
    override val show: Boolean = true,
    override val default: Boolean = false,
    override val order: Int = -1,
    override val isException: Boolean = false
) :
    IDataAdapterEnum, IDataAdapterSymbol {
    KG(value = 1, title = "Kilogramos", symbol = "[kg]", order = 1, default = true),
    PERCENT(value = 2, title = "Porcentaje", symbol = "[%]", order = 2),
    LITER(value = 3, title = "Litros", symbol = "[lts]", order = 3),
    UNDEFINED(value = -1, title = "Indefinido", show = false, isException = true)
}