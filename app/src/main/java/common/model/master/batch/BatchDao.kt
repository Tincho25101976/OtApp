package common.model.master.batch

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.vsg.helper.common.util.dao.IGenericDaoPagingRelationCode

@Dao
abstract class BatchDao : IGenericDaoPagingRelationCode<MasterBatch> {
    //region paging
    @Query("SELECT * FROM ${MasterBatch.ENTITY_NAME} WHERE idItem = :idRelation ORDER BY valueCode")
    abstract override fun viewAllPaging(idRelation: Int): DataSource.Factory<Int, MasterBatch>
    //endregion

    //region items
    @Query("SELECT * FROM ${MasterBatch.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): MasterBatch?
    //endregion

    //region enabled
    @Query("UPDATE ${MasterBatch.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)
    //endregion

    //region search
    @Query("SELECT valueCode FROM ${MasterBatch.ENTITY_NAME} WHERE idItem = :idRelation GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(idRelation: Int): LiveData<List<String>>
    //endregion

    //region parameters
    @Query("SELECT EXISTS(SELECT * FROM ${MasterBatch.ENTITY_NAME} WHERE idItem = :idRelation)")
    abstract override fun viewHasItems(idRelation: Int): Boolean
    //endregion

    //region list
    @Query("SELECT * FROM ${MasterBatch.ENTITY_NAME} WHERE idItem = :idRelation ORDER BY valueCode")
    abstract override fun viewAllSimpleList(idRelation: Int): List<MasterBatch>?
    //endregion

    //region disabled
    @Query("SELECT * FROM ${MasterBatch.ENTITY_NAME} WHERE id < 0")
    abstract override fun viewAll(): LiveData<List<MasterBatch>>?
    //endregion

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${MasterBatch.ENTITY_NAME} WHERE id = :entity)")
    abstract override fun checkExitsEntity(entity: Int): Boolean
    //endregion

    //region batchProduct
    @Transaction
    @Query("SELECT * FROM ${MasterBatch.ENTITY_NAME}  WHERE idItem = :idRelation ORDER BY valueCode")
    abstract fun viewAllSimpleListBatchProductByProduct(idRelation: Int): List<MasterBatch>?

    @Transaction
    @Query("SELECT * FROM ${MasterBatch.ENTITY_NAME}  WHERE id = :idRelation ORDER BY valueCode")
    abstract fun viewAllSimpleBatchProductByBatch(idRelation: Int): MasterBatch?
    //endregion
}