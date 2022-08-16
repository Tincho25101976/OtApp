package com.vsg.ot.common.model.securityDialog.xact.rating

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap

@ExperimentalStdlibApi
class XactRatingViewModel(application: Application) :
    ViewModelGenericParse<XactRatingDao, XactRating>(
        AppDatabase.getInstance(application)?.xactRatingDao()!!,
        application,
        ViewModelStoredMap()
    )