package com.vsg.agendaandpublication.ui.activities.itemProducto.helper

import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.utilities.ui.util.BaseActivity
import com.vsg.utilities.ui.widget.spinner.choose.ChooseIEntitySpinner

@ExperimentalStdlibApi
class ChooseCompany(app: BaseActivity) :
    ChooseIEntitySpinner<Company, CompanyViewModel>(
        app,
        text = R.string.ActivityItemProductCompanyText,
        type = CompanyViewModel::class.java
    )