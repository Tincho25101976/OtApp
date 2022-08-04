package com.vsg.ot.common.model.securityDialog.security.process

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse
import com.vsg.ot.common.model.securityDialog.xact.relationship.XactEventRecord
import com.vsg.ot.common.model.securityDialog.xact.relationship.XactSectorRecord

@Dao
abstract class SecurityProcessDao : DaoGenericOtParse<SecurityProcess>() {
    //region paging
    @Query("SELECT * FROM ${SecurityProcess.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SecurityProcess>
    //endregion

    @Query("SELECT * FROM ${SecurityProcess.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SecurityProcess?

    @Query("SELECT * FROM ${SecurityProcess.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<SecurityProcess>>?

    @Query("SELECT * FROM ${SecurityProcess.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllSimpleList(): List<SecurityProcess>?

    @Query("UPDATE ${SecurityProcess.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${SecurityProcess.ENTITY_NAME} GROUP BY description ORDER BY description")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    @Query("DELETE FROM ${SecurityProcess.ENTITY_NAME}")
    abstract override fun deleteAll()

    @Query("UPDATE sqlite_sequence\n" +
            "SET seq = (SELECT MAX('id') FROM '${SecurityProcess.ENTITY_NAME}')\n" +
            "WHERE name = '${SecurityProcess.ENTITY_NAME}'")
    abstract override fun resetIndexIdentity()

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SecurityProcess.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion

    //region relationShip
    @Transaction
    @Query("SELECT * FROM ${SecurityProcess.ENTITY_NAME} WHERE id = :id ORDER BY description")
    abstract fun viewRecordWithSector(id: Int): XactSectorRecord

    @Transaction
    @Query("SELECT * FROM ${SecurityProcess.ENTITY_NAME} WHERE id = :id ORDER BY description")
    abstract fun viewRecordWithEvent(id: Int): XactEventRecord
    //endregion
}