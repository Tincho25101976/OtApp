package com.vsg.ot.common.model.securityDialog.security.reference

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse

@Dao
abstract class SecurityReferenceDao : DaoGenericOtParse<SecurityReference>() {
    //region paging
    @Query("SELECT * FROM ${SecurityReference.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SecurityReference>
    //endregion

    @Query("SELECT * FROM ${SecurityReference.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SecurityReference?

    @Query("SELECT * FROM ${SecurityReference.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAll(): LiveData<List<SecurityReference>>?

    @Query("SELECT * FROM ${SecurityReference.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllSimpleList(): List<SecurityReference>?

    @Query("UPDATE ${SecurityReference.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${SecurityReference.ENTITY_NAME} GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    @Query("DELETE FROM ${SecurityReference.ENTITY_NAME}")
    abstract override fun deleteAll()

    @Query("UPDATE sqlite_sequence SET seq = (SELECT MAX('id') FROM '${SecurityReference.ENTITY_NAME}') WHERE name = '${SecurityReference.ENTITY_NAME}'")
    abstract override fun resetIndexIdentity()

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SecurityReference.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}