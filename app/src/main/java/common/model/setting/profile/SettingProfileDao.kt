package com.vsg.ot.common.model.setting.profile

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOt

@Dao
abstract class SettingProfileDao : DaoGenericOt<SettingProfile>() {
    //region paging
    @Query("SELECT * FROM ${SettingProfile.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SettingProfile>
    //endregion

    @Query("SELECT * FROM ${SettingProfile.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SettingProfile?

    @Query("SELECT * FROM ${SettingProfile.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<SettingProfile>>?

    @Query("SELECT * FROM ${SettingProfile.ENTITY_NAME} ORDER BY description")
    abstract fun viewAllSimpleList(): List<SettingProfile>?

    @Query("UPDATE ${SettingProfile.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT description FROM ${SettingProfile.ENTITY_NAME} GROUP BY description ORDER BY description")
    abstract fun viewAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SettingProfile.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}