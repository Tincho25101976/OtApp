package common.model.master.item

import android.text.Spanned
import androidx.room.Ignore
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.enumType.TypeUnit
import common.helper.HelperMaster.Companion.toMasterUnit
import common.model.ItemOtBase
import common.model.master.unit.MasterUnit

class MasterItem : ItemOtBase() {
    //region properties
    var item: String = ""
    var unit: MasterUnit? = null
    override val defaultTitle: String
        get() = item
    //endregion

    //region reference
    @Ignore
    override fun aPictureRecyclerAdapter(): Int = R.drawable.pic_product

    override fun descriptionView(): Spanned {
        val sb = StringBuilder()
        sb.toTitleSpanned(item)
        sb.toLineSpanned("Producto", description)
        if (unit != null) sb.toLineSpanned("Unidad", unit?.symbol)
        sb.getBaseDescriptionView()
        return sb.castToHtml()
    }
    //endregion

    //region funtion
    public fun setUnit(unit: TypeUnit, precision: Int = 3) {
        this.unit = unit.toMasterUnit(precision)
    }
    //endregion
}