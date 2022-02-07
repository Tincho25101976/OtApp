package common.model.common.unit

import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.common.unit.type.TypeUnit.Companion.toUnitDetail
import common.model.init.entity.EntityOt
import common.model.common.unit.type.TypeUnit

class Unit(val unit: TypeUnit) : EntityOt<Unit>() {
    //region properties
    val symbol: String get() = unit.toUnitDetail()
    val order: Int get() = unit.order
    val value: Int get() = unit.value

    override val title: String
        get() = unit.title
    //endregion

    override fun oGetDrawablePicture(): Int = R.drawable.pic_unit
    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(title)
            .toLineSpanned("Símbolo", symbol)

    override fun aEquals(other: Any?): Boolean {
        if (other !is Unit) return false
        return unit == other.unit
    }
    //

    companion object{
        const val DEFAULT_VALUE_PRECISION: Int = 3

        fun getDefaultUnit(): Unit = TypeUnit.UNDEFINED.toUnitDetail()
    }
}