package com.vsg.ot.common.model.securityDialog.security.process

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap

@ExperimentalStdlibApi
class SecurityProcessViewModel(application: Application) :
    ViewModelGenericParse<SecurityProcessDao, SecurityProcess>(
        AppDatabase.getInstance(application)?.securityProcess()!!,
        application,
        ViewModelStoredMap()
    )