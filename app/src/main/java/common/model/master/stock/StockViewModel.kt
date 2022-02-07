package common.model.master.stock

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import common.model.init.viewModel.ViewModelGenericOt
import common.model.master.filter.TypeFilterNearExpired

@ExperimentalStdlibApi
class StockViewModel(context: Application) :
    ViewModelGenericOt<StockDao, MasterStock>(
        AppDatabase.getInstance(context)?.stockDao()!!, context
    ) {
    fun getNearExpired(idRelation: Int, filter: TypeFilterNearExpired): List<MasterStock> {
        val data: List<MasterStock> = try {
            val temp = when (filter) {
                TypeFilterNearExpired.COMPANY -> dao.getNearExpiredByCompany(idRelation)
                TypeFilterNearExpired.ITEM -> dao.getNearExpiredByItem(idRelation)
                TypeFilterNearExpired.WAREHOUSE -> dao.getNearExpiredByWarehouse(idRelation)
                TypeFilterNearExpired.SECTION -> dao.getNearExpiredBySection(idRelation)
            }
            temp ?: listOf()

        } catch (e: Exception) {
            listOf()
        }
        return data
    }
}