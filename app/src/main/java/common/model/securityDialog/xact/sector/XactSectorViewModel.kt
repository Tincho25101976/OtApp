package com.vsg.ot.common.model.securityDialog.xact.sector

import android.app.Application
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelation
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap
import common.model.master.item.MasterItemViewModel

@ExperimentalStdlibApi
class XactSectorViewModel(application: Application) :
    ViewModelGenericParse<XactSectorDao, XactSector>(
        AppDatabase.getInstance(application)?.xactSectorDao()!!,
        application,
        ViewModelStoredMap()
    ),
    IViewModelHasItemsRelation {

    override fun viewModelViewHasItems(idRelation: Int): Boolean =
        MasterItemViewModel(context).viewModelViewHasItems(idRelation)
}