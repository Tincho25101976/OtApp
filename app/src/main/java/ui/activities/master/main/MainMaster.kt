package com.vsg.ot.ui.activities.master.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.MasterBatchActivity
import com.vsg.ot.ui.activities.master.MasterCompanyActivity
import com.vsg.ot.ui.activities.master.MasterItemActivity
import com.vsg.ot.ui.activities.master.MasterWarehouseActivity
import com.vsg.ot.ui.activities.master.helper.ChooseCompany
import common.model.master.company.MasterCompany

@ExperimentalStdlibApi
class MainMaster : BaseActivity(R.layout.main_master) {
    private var onEventGetCompany: (() -> MasterCompany)? = null
    private var company: MasterCompany? = null
    private lateinit var chooseCompany: ChooseCompany

    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_company,
                    getString(R.string.ActivityMasterCompanyText),
                    dataClass = MasterCompanyActivity::class.java
                )
                add(
                    R.drawable.pic_product,
                    getString(R.string.ActivityMasterItemText),
                    dataClass = MasterItemActivity::class.java
                )
                addWithAction(
                    R.drawable.pic_batch,
                    getString(R.string.ActivityItemOperationBatchText),
                    dataClass = MasterBatchActivity::class.java,
                    extra = { chooseCompany.item }
                )
                add(
                    R.drawable.pic_warehouse,
                    getString(R.string.ActivityItemOperationWarehouseText),
                    dataClass = MasterWarehouseActivity::class.java
                )
//                addWithAction(
//                    R.drawable.pic_section,
//                    getString(R.string.ActivityItemOperationSectionText),
//                    dataClass = MasterSectionActivity::class.java,
//                    extra = {
//                        chooseCompany.item
//                    }
//                )
            }.makeLayout()
            view
        }
    }

    override fun onExecuteCreate() {
        chooseCompany = ChooseCompany(this).apply {
            spinner.customTheme = CustomSpinner.CustomTheme.BLACK
            onEventItemSelected = {
                this@MainMaster.company = it
                if (it != null) {
//                    this@MainMaster.
                    intent.putExtra(getString(R.string.MsgData), it.id)
                    onEventGetCompany = { it }
                }
            }
        }
        setActivityAsSelector(chooseView = chooseCompany.view!!)
    }
}