package common.model.master.stock

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.vsg.helper.common.util.dao.viewRoom.IGenericDaoPagingRelationViewRoom

@Dao
abstract class StockDao : IGenericDaoPagingRelationViewRoom<MasterStockDTO, StockViewRoom> {
    @Query("SELECT * FROM ${StockViewRoom.ENTITY_NAME} WHERE idCompany = :idRelation")
    abstract override fun viewAllListViewRoom(idRelation: Int): List<StockViewRoom>?

    @Query("SELECT * FROM ${StockViewRoom.ENTITY_NAME} WHERE idCompany = :idRelation")
    abstract override fun viewAllPagingViewRoom(idRelation: Int): DataSource.Factory<Int, StockViewRoom>

    @Query("SELECT productName FROM ${StockViewRoom.ENTITY_NAME} WHERE idCompany = :idRelation GROUP BY productName")
    abstract override fun viewGetAllTextSearch(idRelation: Int): LiveData<List<String>>
}