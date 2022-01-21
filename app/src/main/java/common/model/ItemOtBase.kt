package common.model

import com.vsg.helper.common.util.addItem.ItemBaseAddItem

abstract class ItemOtBase: ItemBaseAddItem() {
    override val isEntityOnlyDefault: Boolean
        get() = true
}