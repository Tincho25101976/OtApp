package com.vsg.ot.common.model.securityDialog.security.group

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap

@ExperimentalStdlibApi
class SecurityGroupViewModel(application: Application) :
    ViewModelGenericParse<SecurityGroupDao, SecurityGroup>(
        AppDatabase.getInstance(application)?.securityGroup()!!,
        application,
        ViewModelStoredMap()
    )