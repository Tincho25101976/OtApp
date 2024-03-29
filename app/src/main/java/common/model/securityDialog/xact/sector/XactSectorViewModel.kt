package com.vsg.ot.common.model.securityDialog.xact.sector

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap

@ExperimentalStdlibApi
class XactSectorViewModel(application: Application) :
    ViewModelGenericParse<XactSectorDao, XactSector>(
        AppDatabase.getInstance(application)?.xactSectorDao()!!,
        application,
        ViewModelStoredMap()
    )
