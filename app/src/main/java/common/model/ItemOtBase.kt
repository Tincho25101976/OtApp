package common.model

import androidx.room.Ignore
import com.vsg.helper.common.util.addItem.ItemBaseAddItem
import com.vsg.ot.R

abstract class ItemOtBase: ItemBaseAddItem() {
    override val isEntityOnlyDefault: Boolean
        get() = true

    //region reference
    @Ignore
    override fun aTitleRecyclerAdapter(): String = title
    @Ignore
    override fun aPictureRecyclerAdapter(): Int = R.drawable.pic_default
    @Ignore
    override fun aTitlePopUpData(): String = title
    //endregion
}