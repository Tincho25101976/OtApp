package common.model.master

import android.text.Spanned
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import common.enumType.TypeUnit
import common.enumType.TypeUnit.Companion.toUnitDetail
import common.model.ItemOtBase

class MasterUnit(val unit: TypeUnit) : ItemOtBase() {
    //region properties
    val symbol: String get() = unit.toUnitDetail()
    val order: Int get() = unit.order
    val value: Int get() = unit.value
    override val defaultTitle: String
        get() = unit.title
    //endregion

    //region reference
    override fun descriptionView(): Spanned {
        val sb = StringBuilder()
        sb.toTitleSpanned(defaultTitle)
        sb.toLineSpanned("Simbolo", symbol)
        return sb.castToHtml()
    }
    //endregion
}