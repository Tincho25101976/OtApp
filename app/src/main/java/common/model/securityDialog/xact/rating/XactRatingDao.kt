package com.vsg.ot.common.model.securityDialog.xact.rating

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse

@Dao
abstract class XactRatingDao : DaoGenericOtParse<XactRating>() {
    //region paging
    @Query("SELECT * FROM ${XactRating.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, XactRating>
    //endregion

    @Query("SELECT * FROM ${XactRating.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): XactRating?

    @Query("SELECT * FROM ${XactRating.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAll(): LiveData<List<XactRating>>?

    @Query("SELECT * FROM ${XactRating.ENTITY_NAME} ORDER BY valueCode")
    abstract override fun viewAllSimpleList(): List<XactRating>?

    @Query("UPDATE ${XactRating.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT valueCode FROM ${XactRating.ENTITY_NAME} GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    @Query("DELETE FROM ${XactRating.ENTITY_NAME}")
    abstract override fun deleteAll()

    @Query(
        "UPDATE sqlite_sequence\n" +
                "SET seq = (SELECT MAX('id') FROM '${XactRating.ENTITY_NAME}')\n" +
                "WHERE name = '${XactRating.ENTITY_NAME}'"
    )
    abstract override fun resetIndexIdentity()

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${XactRating.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}