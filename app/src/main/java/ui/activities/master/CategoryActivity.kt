package com.vsg.agendaandpublication.ui.activities.itemProducto

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.category.Category
import com.vsg.agendaandpublication.common.model.itemProduct.category.CategoryDao
import com.vsg.agendaandpublication.common.model.itemProduct.category.CategoryViewModel
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityCategory
import com.vsg.agendaandpublication.ui.common.itemProduct.category.UICRUDCategory
import com.vsg.utilities.ui.util.CurrentBaseActivityPagingGeneric

@ExperimentalStdlibApi
class CategoryActivity :
    CurrentBaseActivityPagingGeneric<CategoryActivity, CategoryViewModel, CategoryDao, Category, FilterTypeActivityCategory, UICRUDCategory<CategoryActivity>>(
        CategoryViewModel::class.java,
        FilterTypeActivityCategory::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemProductCategoryText
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Category> =
                when (item) {
                    FilterTypeActivityCategory.NAME -> it.filter { s ->
                        s.name.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDCategory(context, operation) }
    }
}