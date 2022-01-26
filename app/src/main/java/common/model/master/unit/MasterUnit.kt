package common.model.master.unit

import android.text.Spanned
import com.ItemOtBase
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import common.enumType.TypeUnit
import common.enumType.TypeUnit.Companion.toUnitDetail

class MasterUnit(val unit: TypeUnit) : ItemOtBase<MasterUnit>() {
    //region properties
    val symbol: String get() = unit.toUnitDetail()
    val order: Int get() = unit.order
    val value: Int get() = unit.value
    override val title: String
        get() = unit.title
    //endregion

    //region reference
    override fun descriptionView(): Spanned {
        val sb = StringBuilder()
        sb.toTitleSpanned(title)
        sb.toLineSpanned("Simbolo", symbol)
        return sb.castToHtml()
    }
    override fun reference(): Spanned {
        val sb = StringBuilder()
        sb.toTitleSpanned(title)
        sb.toLineSpanned("Simbolo", symbol)
        return sb.castToHtml()
    }
    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterUnit) return false
        return unit == other.unit
    }
    //endregion
}