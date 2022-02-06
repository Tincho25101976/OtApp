package common.model.master.stock

import android.app.Application
import androidx.room.Query
import com.vsg.agendaandpublication.common.data.AppDatabase
import common.model.init.viewModel.ViewModelGenericOt
import common.model.master.batch.MasterBatch
import common.model.master.item.MasterItem

@ExperimentalStdlibApi
abstract class StockViewModel(context: Application) :
    ViewModelGenericOt<StockDao, MasterStock>(
        AppDatabase.getInstance(context)?.stockDao()!!, context
    ) {
    @Query(
        "SELECT " +
                "st.* " +
                "FROM ${MasterStock.ENTITY_NAME} st " +
                "INNER JOIN ${MasterBatch.ENTITY_NAME} bt " +
                "   ON bt.id = st.idBatch AND bt.idCompany = st.idCompany " +
                "INNER JOIN ${MasterItem.ENTITY_NAME} p " +
                "   ON p.id = st.idItem AND p.idCompany = st.idCompany AND bt.idItem = p.id " +
                "WHERE idCompany = :idRelation " +
                "       AND DATE() <= bt.dueDate " +
                "       AND "
        "ORDER BY description"
    )
    abstract fun getNearExpired(idRelation: Int): List<MasterItem>?
}