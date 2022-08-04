package com.vsg.ot.common.model.securityDialog.security.item

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap

@ExperimentalStdlibApi
class SecurityItemViewModel(application: Application) :
    ViewModelGenericParse<SecurityItemDao, SecurityItem>(
        AppDatabase.getInstance(application)?.securityItem()!!,
        application,
        ViewModelStoredMap()
    )