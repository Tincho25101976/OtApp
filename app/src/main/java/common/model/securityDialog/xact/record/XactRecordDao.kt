package com.vsg.ot.common.model.securityDialog.xact.record

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.vsg.ot.common.model.securityDialog.xact.relationship.XactEventRecord
import com.vsg.ot.common.model.securityDialog.xact.relationship.XactSectorRecord
import common.model.init.dao.DaoGenericOt

@Dao
abstract class XactRecordDao : DaoGenericOt<XactRecord>() {
    //region paging
    @Query("SELECT * FROM ${XactRecord.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, XactRecord>
    //endregion

    @Query("SELECT * FROM ${XactRecord.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): XactRecord?

    @Query("SELECT * FROM ${XactRecord.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<XactRecord>>?

    @Query("SELECT * FROM ${XactRecord.ENTITY_NAME} ORDER BY description")
    abstract fun viewAllSimpleList(): List<XactRecord>?

    @Query("UPDATE ${XactRecord.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT caption FROM ${XactRecord.ENTITY_NAME} GROUP BY description ORDER BY description")
    abstract fun viewAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${XactRecord.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion

    //region relationShip
    @Transaction
    @Query("SELECT * FROM ${XactRecord.ENTITY_NAME} WHERE id = :id ORDER BY description")
    abstract fun viewRecordWithSector(id: Int): XactSectorRecord

    @Transaction
    @Query("SELECT * FROM ${XactRecord.ENTITY_NAME} WHERE id = :id ORDER BY description")
    abstract fun viewRecordWithEvent(id: Int): XactEventRecord
    //endregion
}