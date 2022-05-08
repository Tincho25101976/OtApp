package common.model.master.company

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import common.model.init.dao.DaoGenericOt
import common.model.master.relationship.CompanyBatch
import common.model.master.relationship.CompanyItem

@Dao
abstract class MasterCompanyDao : DaoGenericOt<MasterCompany>() {
    //region relationShip
    @Transaction
    @Query("SELECT * FROM ${MasterCompany.ENTITY_NAME} WHERE id = :id ORDER BY description")
    abstract fun viewCompanyWithProduct(id: Int): DataSource.Factory<Int, CompanyItem>

    @Transaction
    @Query("SELECT * FROM ${MasterCompany.ENTITY_NAME} WHERE id = :id ORDER BY description")
    abstract fun viewCompanyWithBatch(id: Int): DataSource.Factory<Int, CompanyBatch>
    //endregion

    //region paging
    @Query("SELECT * FROM ${MasterCompany.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, MasterCompany>
    //endregion

    @Query("SELECT * FROM ${MasterCompany.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): MasterCompany?

    @Query("SELECT * FROM ${MasterCompany.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<MasterCompany>>?

    @Query("SELECT * FROM ${MasterCompany.ENTITY_NAME} ORDER BY description")
    abstract fun viewAllSimpleList(): List<MasterCompany>?

    @Query("UPDATE ${MasterCompany.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${MasterCompany.ENTITY_NAME} GROUP BY description ORDER BY description")
    abstract fun viewAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${MasterCompany.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}