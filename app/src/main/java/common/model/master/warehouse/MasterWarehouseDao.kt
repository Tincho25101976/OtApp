package common.model.master.warehouse

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.vsg.helper.common.util.dao.IDaoAllUpdateIsDefault
import common.model.init.dao.DaoGenericOtCompany

@Dao
abstract class MasterWarehouseDao : DaoGenericOtCompany<MasterWarehouse>(),
    IDaoAllUpdateIsDefault {
    //region paging
    @Query("SELECT * FROM ${MasterWarehouse.ENTITY_NAME} WHERE idCompany = :idRelation ORDER BY valueCode")
    abstract override fun viewAllPaging(idRelation: Int): DataSource.Factory<Int, MasterWarehouse>
    //endregion

    //region items
    @Query("SELECT * FROM ${MasterWarehouse.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): MasterWarehouse?
    //endregion

    //region enabled
    @Query("UPDATE ${MasterWarehouse.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)
    //endregion

    //region search
    @Query("SELECT valueCode FROM ${MasterWarehouse.ENTITY_NAME} WHERE idCompany = :idRelation GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(idRelation: Int): LiveData<List<String>>
    //endregion

    //region parameters
    @Query("SELECT EXISTS(SELECT * FROM ${MasterWarehouse.ENTITY_NAME} WHERE idCompany = :idRelation)")
    abstract override fun viewHasItems(idRelation: Int): Boolean
    //endregion

    //region list
    @Query("SELECT * FROM ${MasterWarehouse.ENTITY_NAME} WHERE idCompany = :idRelation ORDER BY valueCode")
    abstract override fun viewAllSimpleList(idRelation: Int): List<MasterWarehouse>?

    @Query("SELECT * FROM ${MasterWarehouse.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, MasterWarehouse>

    @Query("SELECT * FROM ${MasterWarehouse.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllSimpleList(): List<MasterWarehouse>?
    //endregion

    //region disabled
    @Query("SELECT * FROM ${MasterWarehouse.ENTITY_NAME} WHERE id < 0")
    abstract override fun viewAll(): LiveData<List<MasterWarehouse>>?
    //endregion

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${MasterWarehouse.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion

    //region updateIsDefault
    @Transaction
    @Query("UPDATE ${MasterWarehouse.ENTITY_NAME} SET isDefault = CASE id WHEN :idEntity THEN 1 ELSE 0 END WHERE idCompany = :idRelation ")
    abstract override fun updateSetAllIsDefault(idEntity: Int, idRelation: Int)
    //endregion
}