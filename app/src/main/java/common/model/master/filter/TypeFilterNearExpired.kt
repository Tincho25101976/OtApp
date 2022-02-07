package common.model.master.filter

import com.vsg.helper.ui.adapter.paging.IDataAdapterValue

enum class TypeFilterNearExpired(override val value: Int, override val title: String) :
    IDataAdapterValue {
    COMPANY(value = 1, title = "Company"),
    ITEM(value = 1, title = "Ítem"),
    WAREHOUSE(value = 1, title = "Warehouse"),
    SECTION(value = 4, title = "Section")
}