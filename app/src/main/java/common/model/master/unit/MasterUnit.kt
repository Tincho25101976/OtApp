package common.model.master.unit

import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.master.unit.type.TypeUnit.Companion.toUnitDetail
import common.model.init.entity.EntityOt
import common.model.master.unit.type.TypeUnit

class MasterUnit(val unit: TypeUnit) : EntityOt<MasterUnit>() {
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
        if (other !is MasterUnit) return false
        return unit == other.unit
    }
    //endregion
}