package com.vsg.ot.common.model.securityDialog.security.reference

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap

@ExperimentalStdlibApi
class SecurityReferenceViewModel(application: Application) :
    ViewModelGenericParse<SecurityReferenceDao, SecurityReference>(
        AppDatabase.getInstance(application)?.securityReference()!!,
        application,
        ViewModelStoredMap()
    )