package common.model.init.viewModel

import android.app.Application
import com.vsg.helper.common.model.viewModel.IViewModelStoredMap
import common.model.master.batch.BatchViewModel
import common.model.master.section.SectionViewModel
import common.model.master.warehouse.WarehouseViewModel
import common.model.master.item.ItemViewModel
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.util.DataMakeViewModelView
import common.model.master.batch.MasterBatch
import common.model.master.company.CompanyViewModel
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
import common.model.master.warehouse.MasterWarehouse
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class ViewModelStoredMap: IViewModelStoredMap {

    private fun storedInstanceOfViewModelView(context: Application): MutableList<DataMakeViewModelView<*, *>> {
        val data: MutableList<DataMakeViewModelView<*, *>> = mutableListOf()
        data.add(DataMakeViewModelView(typeOf<MasterItem>(), ItemViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<MasterCompany>(), CompanyViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<MasterBatch>(), BatchViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<MasterWarehouse>(), WarehouseViewModel(context)))
        data.add(DataMakeViewModelView(typeOf<MasterSection>(), SectionViewModel(context)))
        return data
    }

    override fun getInstanceOfIViewModelView(type: KType, context: Application): IViewModelView<*>? {
        val temp = storedInstanceOfViewModelView(context)
            .filter { it.viewModel is IViewModelView<*> }
            .firstOrNull { it.isType(type) }

        return when (temp == null) {
            true -> null
            false -> temp.viewModel as IViewModelView<*>
        }
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(
        type: KType,
        context: Application
    ): IViewModelAllSimpleListIdRelation<*>? {
        val temp = storedInstanceOfViewModelView(context)
            .filter { it.viewModel is IViewModelAllSimpleListIdRelation<*> }
            .firstOrNull { it.isType(type) }

        return when (temp == null) {
            true -> null
            false -> temp.viewModel as IViewModelAllSimpleListIdRelation<*>
        }
    }
    companion object {

    }
}