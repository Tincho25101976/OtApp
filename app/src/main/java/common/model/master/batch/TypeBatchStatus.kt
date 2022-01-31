package common.model.master.batch

import android.graphics.Color

enum class TypeBatchStatus(val value: Int, val data: String, val color: Int) {
    RIGHT(0, "Apto", Color.rgb(87, 229, 104)),
    EXPIRED(1, "Vencido", Color.rgb(214, 168, 69)),
    NEAR_EXPIRY(2, "Próximo a vencer", Color.rgb(226, 214, 76)),
    UNDEFINED(-1, "Indefinido", Color.TRANSPARENT)
}