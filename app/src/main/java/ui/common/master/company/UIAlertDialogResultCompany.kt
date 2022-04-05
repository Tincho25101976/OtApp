package com.vsg.agendaandpublication.ui.common.itemProduct.company

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityCompany
import com.vsg.utilities.ui.popup.select.UIAlertDialogResultEntity
import com.vsg.utilities.ui.util.BaseActivity

@ExperimentalStdlibApi
class UIAlertDialogResultCompany<TActivity>(activity: TActivity, viewModel: CompanyViewModel) :
    UIAlertDialogResultEntity<TActivity, CompanyViewModel, Company, FilterTypeActivityCompany>(
        activity, viewModel, FilterTypeActivityCompany::class.java
    ) where TActivity : BaseActivity {
    init {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Company> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s ->
                        s.name.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
    }
}