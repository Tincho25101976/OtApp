package com.vsg.ot.common.model.securityDialog.xact.sector

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent

@Dao
abstract class XactSectorDao : DaoGenericOtParse<XactSector>() {
    //region paging
    @Query("SELECT * FROM ${XactSector.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, XactSector>
    //endregion

    @Query("SELECT * FROM ${XactSector.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): XactSector?

    @Query("SELECT * FROM ${XactSector.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAll(): LiveData<List<XactSector>>?

    @Query("SELECT * FROM ${XactSector.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllSimpleList(): List<XactSector>?

    @Query("UPDATE ${XactSector.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${XactSector.ENTITY_NAME} GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    @Query("DELETE FROM ${XactSector.ENTITY_NAME}")
    abstract override fun deleteAll()
    @Query("UPDATE sqlite_sequence\n" +
            "SET seq = (SELECT MAX('id') FROM '${XactSector.ENTITY_NAME}')\n" +
            "WHERE name = '${XactSector.ENTITY_NAME}'")
    abstract override fun resetIndexIdentity()

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${XactSector.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}