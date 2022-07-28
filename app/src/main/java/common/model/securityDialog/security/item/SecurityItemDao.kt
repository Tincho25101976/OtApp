package com.vsg.ot.common.model.securityDialog.security.item

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Query
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse

abstract class SecurityItemDao : DaoGenericOtParse<SecurityItem>() {
    //region paging
    @Query("SELECT * FROM ${SecurityItem.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SecurityItem>
    //endregion

    @Query("SELECT * FROM ${SecurityItem.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SecurityItem?

    @Query("SELECT * FROM ${SecurityItem.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAll(): LiveData<List<SecurityItem>>?

    @Query("SELECT * FROM ${SecurityItem.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllSimpleList(): List<SecurityItem>?

    @Query("UPDATE ${SecurityItem.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${SecurityItem.ENTITY_NAME} GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    @Query("DELETE FROM ${SecurityItem.ENTITY_NAME}")
    abstract override fun deleteAll()

    @Query("UPDATE sqlite_sequence SET seq = (SELECT MAX('id') FROM '${SecurityItem.ENTITY_NAME}') WHERE name = '${SecurityItem.ENTITY_NAME}'")
    abstract override fun resetIndexIdentity()

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SecurityItem.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}