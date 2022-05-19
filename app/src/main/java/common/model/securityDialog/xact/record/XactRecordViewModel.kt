package com.vsg.ot.common.model.securityDialog.xact.record

import android.app.Application
import androidx.lifecycle.LiveData
import com.vsg.helper.common.model.viewModel.ViewModelGeneric
import com.vsg.helper.common.util.viewModel.*
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap
import common.model.master.item.MasterItemViewModel
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class XactRecordViewModel(application: Application) :
    ViewModelGeneric<XactRecordDao, XactRecord>(
        AppDatabase.getInstance(application)?.xactRecordDao()!!,
        application,
        ViewModelStoredMap()
    ),
    IViewModelAllTextSearch,
    IViewModelView<XactRecord>,
    IViewModelAllSimpleList<XactRecord>,
    IViewModelAllSimpleListWithRelation<XactRecord>,
    IViewModelHasItemsRelation {

    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewGetAllTextSearch()
    }

    override fun viewModelViewAllSimpleList(): List<XactRecord> =
        dao.viewAllSimpleList() ?: listOf()

    override fun viewModelViewListWithRelations(): LiveData<List<XactRecord>>? {
        val data: LiveData<List<XactRecord>> = dao.viewAll() ?: return null
        if (data.value == null || !data.value!!.any()) return null
        data.value!!.forEach {
            it.event = getEntityWithRelation(it.idEvent)
            it.sector = getEntityWithRelation(it.idSector)
        }
        return data
    }

    override fun viewModelViewWithRelations(id: Int): XactRecord? {
        val data = dao.view(id) ?: return null
        data.event = getEntityWithRelation(data.idEvent)
        data.sector = getEntityWithRelation(data.idSector)
        return data
    }

    override fun viewModelViewHasItems(idRelation: Int): Boolean =
        MasterItemViewModel(context).viewModelViewHasItems(idRelation)
}