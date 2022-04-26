package com.vsg.ot.common.model.securityDialog.xact.process

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOt

@Dao
abstract class XactProcessDao : DaoGenericOt<XactProcess>() {
    //region paging
    @Query("SELECT * FROM ${XactProcess.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, XactProcess>
    //endregion

    @Query("SELECT * FROM ${XactProcess.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): XactProcess?

    @Query("SELECT * FROM ${XactProcess.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAll(): LiveData<List<XactProcess>>?

    @Query("SELECT * FROM ${XactProcess.ENTITY_NAME} ORDER BY valueCode")
    abstract fun viewAllSimpleList(): List<XactProcess>?

    @Query("UPDATE ${XactProcess.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${XactProcess.ENTITY_NAME} GROUP BY valueCode ORDER BY valueCode")
    abstract fun viewAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${XactProcess.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}