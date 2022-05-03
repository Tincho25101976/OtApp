package com.vsg.ot.common.model.securityDialog.xact.event

import android.app.Application
import androidx.lifecycle.LiveData
import com.vsg.ot.common.data.AppDatabase
import com.vsg.helper.common.model.viewModel.ViewModelGeneric
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleList
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventDao
import common.model.init.viewModel.ViewModelStoredMap
import common.model.master.item.MasterItemViewModel
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class XactEventViewModel(application: Application) :
    ViewModelGeneric<XactEventDao, XactEvent>(
        AppDatabase.getInstance(application)?.xactEventDao()!!,
        application,
        ViewModelStoredMap()
    ),
    IViewModelAllTextSearch,
    IViewModelView<XactEvent>,
    IViewModelAllSimpleList<XactEvent>,
    IViewModelHasItemsRelation {

    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewAllTextSearch()
    }

    override fun viewModelViewAllSimpleList(): List<XactEvent> =
        dao.viewAllSimpleList() ?: listOf()

    override fun viewModelViewHasItems(idRelation: Int): Boolean =
        MasterItemViewModel(context).viewModelViewHasItems(idRelation)
}