package common.model.master

import android.text.Spanned
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import common.enumType.TypeUnit
import common.model.ItemOtBase

class MasterUnit(val unit: TypeUnit) : ItemOtBase() {
    //region properties
    val symbol: String get() = unit.symbol
    override var isDefault: Boolean
        get() = unit.default
        set(value) {}
    val order: Int get() = unit.order
    val value: Int get() = unit.value

    override var description: String
        get() = unit.title
        set(value) {}
    //endregion

    //region reference
    override fun descriptionView(): Spanned {
        val sb = StringBuilder()
        sb.toTitleSpanned(description)
        sb.getBaseDescriptionView()
        return sb.castToHtml()
    }
    //endregion
}