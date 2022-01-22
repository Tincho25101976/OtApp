package common.model

import androidx.room.Ignore
import com.vsg.helper.common.util.addItem.ItemBaseAddItem
import com.vsg.ot.R
import common.enumType.TypeUnit
import common.model.master.MasterUnit

abstract class ItemOtBase: ItemBaseAddItem() {
    override val isEntityOnlyDefault: Boolean
        get() = true

    //region reference
    @Ignore
    override fun aTitleRecyclerAdapter(): String = defaultTitle
    @Ignore
    override fun aPictureRecyclerAdapter(): Int = R.drawable.pic_default
    @Ignore
    override fun aTitlePopUpData(): String = defaultTitle
    //endregion

    companion object{
        protected fun TypeUnit.toMasterUnit(presicion:Int = 3): MasterUnit = MasterUnit(this).apply { precision = presicion }
    }
}