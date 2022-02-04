package common.model.master.section

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.util.viewModel.IViewModelUpdateIsDefault
import common.model.init.viewModel.ViewModelGenericForCode

@ExperimentalStdlibApi
class SectionViewModel(context: Application) :
    ViewModelGenericForCode<SectionDao, MasterSection>(
        AppDatabase.getInstance(context)?.sectionDao()!!, context
    ),
    IViewModelUpdateIsDefault<MasterSection> {

    override fun viewModelSetIsDefault(item: MasterSection) =
        dao.updateSetAllIsDefault(item.id, item.idWarehouse)

    fun viewModelViewAllSimpleListByCompany(idRelation: Int): List<MasterSection> =
        dao.viewAllSimpleListByCompany(idRelation) ?: listOf()

    fun viewModelViewAllSimpleList(): List<MasterSection> =
        dao.viewAllSimpleList() ?: listOf()
}