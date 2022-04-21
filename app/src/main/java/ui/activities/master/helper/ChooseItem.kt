package com.vsg.ot.ui.activities.master.helper

import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.spinner.choose.ChooseIEntitySpinnerWithParameter
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemViewModel

@ExperimentalStdlibApi
class ChooseItem(app: BaseActivity, company: MasterCompany) :
    ChooseIEntitySpinnerWithParameter<MasterItem, MasterItemViewModel, MasterCompany>(
        app,
        text = R.string.ActivityMasterItemText,
        type = MasterItemViewModel::class.java,
        company
    )