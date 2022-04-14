package com.vsg.ot.ui.activities.master.helper

import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.spinner.choose.ChooseIEntitySpinner
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyViewModel

@ExperimentalStdlibApi
class ChooseCompany(app: BaseActivity) :
    ChooseIEntitySpinner<MasterCompany, MasterCompanyViewModel>(
        app,
        text = R.string.ActivityMasterCompanyText,
        type = MasterCompanyViewModel::class.java
    )