package com.vsg.ot.common.model.securityDialog.xact.event

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import common.model.init.dao.DaoGenericOt

@Dao
abstract class XactEventDao : DaoGenericOtParse<XactEvent>() {
    //region paging
    @Query("SELECT * FROM ${XactEvent.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, XactEvent>
    //endregion

    @Query("SELECT * FROM ${XactEvent.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): XactEvent?

    @Query("SELECT * FROM ${XactEvent.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAll(): LiveData<List<XactEvent>>?

    @Query("SELECT * FROM ${XactEvent.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllSimpleList(): List<XactEvent>?

    @Query("UPDATE ${XactEvent.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${XactEvent.ENTITY_NAME} GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    @Query("DELETE FROM ${XactEvent.ENTITY_NAME}")
    abstract override fun deleteAll()
    @Query("UPDATE sqlite_sequence\n" +
            "SET seq = (SELECT MAX('id') FROM '${XactEvent.ENTITY_NAME}')\n" +
            "WHERE name = '${XactEvent.ENTITY_NAME}'")
    abstract override fun resetIndexIdentity()

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${XactEvent.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}