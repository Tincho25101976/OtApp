package common.model.master.batch

import android.graphics.Color
import com.vsg.helper.ui.adapter.IDataAdapterEnum

enum class TypeBatchStatus(
    override val value: Int, override val title: String, val color: Int,
    override val order: Int, override val show: Boolean = true,
    override val default: Boolean = false, override val isException: Boolean = false
) :
    IDataAdapterEnum {
    RIGHT(0, "Apto", Color.rgb(87, 229, 104), order = 1, default = true),
    EXPIRED(1, "Vencido", Color.rgb(214, 168, 69), order = 2),
    NEAR_EXPIRY(2, "Próximo a vencer", Color.rgb(226, 214, 76), order = 3),
    UNDEFINED(-1, "Indefinido", Color.TRANSPARENT, order = 1000, isException = true, show = false)
}