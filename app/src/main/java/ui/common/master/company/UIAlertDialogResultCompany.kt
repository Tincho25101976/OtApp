package com.vsg.ot.ui.common.master.company

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.popup.select.UIAlertDialogResultEntity
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityCompany
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyViewModel

@ExperimentalStdlibApi
class UIAlertDialogResultCompany<TActivity>(
    activity: TActivity,
    viewModel: MasterCompanyViewModel
) :
    UIAlertDialogResultEntity<TActivity, MasterCompanyViewModel, MasterCompany, FilterTypeActivityCompany>(
        activity, viewModel, FilterTypeActivityCompany::class.java
    ) where TActivity : BaseActivity {
    init {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<MasterCompany> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s ->
                        s.valueCode.contains(
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