package com.vsg.ot.common.model.securityDialog.security.group

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse

@Dao
abstract class SecurityGroupDao : DaoGenericOtParse<SecurityGroup>() {
    //region paging
    @Query("SELECT * FROM ${SecurityGroup.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SecurityGroup>
    //endregion

    @Query("SELECT * FROM ${SecurityGroup.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SecurityGroup?

    @Query("SELECT * FROM ${SecurityGroup.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAll(): LiveData<List<SecurityGroup>>?

    @Query("SELECT * FROM ${SecurityGroup.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllSimpleList(): List<SecurityGroup>?

    @Query("UPDATE ${SecurityGroup.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${SecurityGroup.ENTITY_NAME} GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    @Query("DELETE FROM ${SecurityGroup.ENTITY_NAME}")
    abstract override fun deleteAll()

    @Query("UPDATE sqlite_sequence SET seq = (SELECT MAX('id') FROM '${SecurityGroup.ENTITY_NAME}') WHERE name = '${SecurityGroup.ENTITY_NAME}'")
    abstract override fun resetIndexIdentity()

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SecurityGroup.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}