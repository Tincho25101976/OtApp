package common.model.master.filter

import com.vsg.helper.ui.adapter.paging.IDataAdapterValue

enum class TypeFilterHasProductItems(override val value: Int, override val title: String) :
    IDataAdapterValue {
    BATCH(value = 1, title = "Batch")
}