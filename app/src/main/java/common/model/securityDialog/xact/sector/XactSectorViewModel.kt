package com.vsg.ot.common.model.securityDialog.xact.sector

import android.app.Application
import androidx.lifecycle.LiveData
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.model.viewModel.ViewModelGeneric
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleList
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import common.model.init.viewModel.ViewModelStoredMap
import common.model.master.item.MasterItemViewModel
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class XactSectorViewModel(application: Application) :
    ViewModelGeneric<XactSectorDao, XactSector>(
        AppDatabase.getInstance(application)?.xactSectorDao()!!,
        application,
        ViewModelStoredMap()
    ),
    IViewModelAllTextSearch,
    IViewModelView<XactSector>,
    IViewModelAllSimpleList<XactSector>,
    IViewModelHasItemsRelation {

    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewAllTextSearch()
    }

    override fun viewModelViewAllSimpleList(): List<XactSector> =
        dao.viewAllSimpleList() ?: listOf()

    override fun viewModelViewHasItems(idRelation: Int): Boolean =
        MasterItemViewModel(context).viewModelViewHasItems(idRelation)
}