package common.model.master.unit

import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import common.enumType.TypeUnit
import common.enumType.TypeUnit.Companion.toUnitDetail
import common.model.ItemOtBase

class MasterUnit(val unit: TypeUnit) : ItemOtBase<MasterUnit>() {
    //region properties
    val symbol: String get() = unit.toUnitDetail()
    val order: Int get() = unit.order
    val value: Int get() = unit.value
    override var description: String
        get() = unit.title
        set(value) {}
    override val title: String
        get() = unit.title
    //endregion

    //region methods
    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(title)
            .toLineSpanned("Simbolo", symbol)

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterUnit) return false
        return unit == other.unit
    }
    //endregion
}