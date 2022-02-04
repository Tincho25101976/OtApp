package common.model.master.filter

import com.vsg.helper.ui.adapter.paging.IDataAdapterValue

enum class TypeFilterHasWarehouseItems(override val value: Int, override val title: String) :
    IDataAdapterValue {
    SECTION(value = 1, title = "Section")
}