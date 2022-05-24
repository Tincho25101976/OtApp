package com.vsg.ot.common.model.securityDialog.xact.record

import android.app.Application
import androidx.lifecycle.LiveData
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleList
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListWithRelation
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelation
import com.vsg.helper.helper.permission.HelperPerminission.Static.checkedPermissionStorage
import com.vsg.ot.common.data.AppDatabase
import common.model.init.viewModel.ViewModelStoredMap
import common.model.master.item.MasterItemViewModel
import java.io.File
import java.io.FileOutputStream

@ExperimentalStdlibApi
class XactRecordViewModel(application: Application) :
    ViewModelGenericParse<XactRecordDao, XactRecord>(
        AppDatabase.getInstance(application)?.xactRecordDao()!!,
        application,
        ViewModelStoredMap()
    ),
    IViewModelAllSimpleList<XactRecord>,
    IViewModelAllSimpleListWithRelation<XactRecord>,
    IViewModelHasItemsRelation
//    IViewModelGetPDFFile
{
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