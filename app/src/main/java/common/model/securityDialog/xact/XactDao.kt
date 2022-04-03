package common.model.securityDialog.xact

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOt

@Dao
abstract class XactDao : DaoGenericOt<Xact>() {
    //region paging
    @Query("SELECT * FROM ${Xact.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, Xact>
    //endregion

    @Query("SELECT * FROM ${Xact.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): Xact?

    @Query("SELECT * FROM ${Xact.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<Xact>>?

    @Query("SELECT * FROM ${Xact.ENTITY_NAME} ORDER BY description")
    abstract fun viewAllSimpleList(): List<Xact>?

    @Query("UPDATE ${Xact.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT description FROM ${Xact.ENTITY_NAME} GROUP BY description ORDER BY description")
    abstract fun viewAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${Xact.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}