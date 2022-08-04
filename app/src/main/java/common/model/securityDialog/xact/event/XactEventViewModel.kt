package com.vsg.ot.common.model.securityDialog.xact.event

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap

@ExperimentalStdlibApi
class XactEventViewModel(application: Application) :
    ViewModelGenericParse<XactEventDao, XactEvent>(
        AppDatabase.getInstance(application)?.xactEventDao()!!,
        application,
        ViewModelStoredMap()
    )
//    IViewModelHasItemsRelation {
//
//    override fun viewModelViewHasItems(idRelation: Int): Boolean =
//        MasterItemViewModel(context).viewModelViewHasItems(idRelation)
//}