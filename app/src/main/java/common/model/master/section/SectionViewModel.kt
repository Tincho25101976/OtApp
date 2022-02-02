package com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.utilities.common.util.viewModel.IViewModelUpdateIsDefault
import common.model.master.section.MasterSection
import common.model.master.section.SectionDao

@ExperimentalStdlibApi
class SectionViewModel(context: Application) :
    ViewModelGenericForCode<SectionDao, MasterSection>(
        AppDatabase.getInstance(context)?.sectionDao()!!, context
    ), IViewModelUpdateIsDefault<MasterSection> {
    override fun viewModelInsert(item: MasterSection): Boolean {
        val code = viewModelEncode(item) ?: return false
        return super.viewModelInsert(code)
    }

    override fun viewModelEncode(item: MasterSection): MasterSection? {
        item.number = viewModelNextAutoCode(item.idWarehouse)
        item.prefix = MasterSection.DEFAULT_PREFIX
        item.valueCode = item.makeGenericValueCode()
        if (item.valueCode.isEmpty()) return null
        return item
    }

    override fun viewModelSetIsDefault(item: MasterSection) =
        dao.updateSetAllIsDefault(item.id, item.idWarehouse)

    fun viewModelViewAllSimpleListByCompany(idRelation: Long): List<MasterSection> =
        dao.viewAllSimpleListByCompany(idRelation) ?: listOf()
    fun viewModelViewAllSimpleList(): List<MasterSection> =
        dao.viewAllSimpleList() ?: listOf()
}