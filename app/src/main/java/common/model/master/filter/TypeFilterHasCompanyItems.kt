package common.model.master.filter

import com.vsg.helper.ui.adapter.paging.IDataAdapterValue

enum class TypeFilterHasCompanyItems(override val value: Int, override val title: String) :
    IDataAdapterValue {
    PRODUCT(value = 1, title = "Product"),
    WAREHOUSE(value = 2, title = "Warehouse"),
}