package common.model.master.batch

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.viewModel.IViewModelCRUD
import com.vsg.helper.common.util.viewModel.util.FilterMemberInclude
import com.vsg.helper.helper.Helper.Companion.toCount
import common.model.init.viewModel.ViewModelGenericOt
import common.model.master.batch.type.TypeBatchStatus
import common.model.master.company.MasterCompany
import common.model.master.filter.TypeFilterNearExpired
import common.model.master.item.MasterItem
import common.model.master.item.ItemViewModel
import common.model.master.stock.MasterStock
import common.model.master.stock.StockViewModel
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class BatchViewModel(context: Application) :
    ViewModelGenericOt<BatchDao, MasterBatch>(
        AppDatabase.getInstance(context)?.batchDao()!!, context
    ) {

    //region add
    override fun viewModelInsert(item: MasterBatch): Boolean {
        val code = viewModelEncode(item) ?: return false
        return super.viewModelInsert(code)
    }
    //endregion

    //region embebed
    fun viewModelGetBatchProductByProduct(idRelation: Int): List<MasterBatch>? {
        val list = dao.viewAllSimpleListBatchProductByProduct(idRelation) ?: return null
        val temp: MutableList<MasterBatch?> = mutableListOf()
        list.mapTo(temp) {
            viewModelBatchProduct(it.id)
        }
        return temp.filterNotNull()
    }

    fun viewModelGetBatchProductByBatch(idRelation: Int): MasterBatch? {
        val temp = dao.viewAllSimpleBatchProductByBatch(idRelation) ?: return null
        return viewModelBatchProduct(temp.id)
    }

    private fun viewModelBatchProduct(id: Int): MasterBatch? {
        return this.viewModelView(
            id,
            include = arrayOf(FilterMemberInclude(entity = typeOf<MasterItem>()).apply {
                addEntity(
                    typeOf<Unit>()
                )
            })
        )
    }
    //endregion

    fun viewModelGetProduct(idRelation: Int): MasterItem? =
        ItemViewModel(context).viewModelView(idRelation)

    //region transaction

    //region alertaVencimiento
    fun viewModelGetDueDateAlert(empresa: MasterCompany): List<MasterStock>? {
        var e: List<MasterStock>? = null
        try {
            e = StockViewModel(context).getNearExpired(empresa.id, TypeFilterNearExpired.COMPANY)
            if (!e.any()) {
                onRaiseException("No hay stock disponible para esta empresa", 2)
                return null
            }
            e = e.filter { s -> s.expirationStatus == TypeBatchStatus.NEAR_EXPIRY }.toList()
        } catch (ex: Exception) {
            onRaiseException(ex, 3)
        }
        return e
    }
    //endregion

    //region privates

    //region add Operation
    private fun <T : IEntity> addItem(list: List<T>, viewModel: IViewModelCRUD<T>): Boolean {
        var count = 0
        var temp = 0
        list.forEach { tr ->
            temp++
            count += viewModel.viewModelInsert(tr).toCount()
        }
        return count == temp
    }
    //endregion

    //endregion

    //endregion
}