package common.model.master.item

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOtCompany

@Dao
abstract class MasterItemDao : DaoGenericOtCompany<MasterItem>() {

    //region paging
    @Query("SELECT * FROM ${MasterItem.ENTITY_NAME} WHERE id > 0 ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, MasterItem>

    @Query("SELECT * FROM ${MasterItem.ENTITY_NAME} WHERE idCompany = :idRelation ORDER BY description")
    abstract override fun viewAllPaging(idRelation: Int): DataSource.Factory<Int, MasterItem>
    //endregion

    @Query("SELECT * FROM ${MasterItem.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): MasterItem?

    @Query("SELECT * FROM ${MasterItem.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<MasterItem>>?

    @Query("SELECT * FROM ${MasterItem.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllSimpleList(): List<MasterItem>?

    @Query("UPDATE ${MasterItem.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT description FROM ${MasterItem.ENTITY_NAME} WHERE idCompany = :idRelation GROUP BY description ORDER BY description")
    abstract override fun viewGetAllTextSearch(idRelation: Int): LiveData<List<String>>

    @Query("SELECT EXISTS(SELECT * FROM ${MasterItem.ENTITY_NAME} WHERE idCompany = :idRelation)")
    abstract override fun viewHasItems(idRelation: Int): Boolean

    @Query("SELECT * FROM ${MasterItem.ENTITY_NAME} WHERE idCompany = :idRelation ORDER BY description")
    abstract override fun viewAllSimpleList(idRelation: Int): List<MasterItem>?

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${MasterItem.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}