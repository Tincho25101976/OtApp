package com.vsg.ot.common.model.setting.user

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOt

@Dao
abstract class SettingUserDao : DaoGenericOt<SettingUser>() {
    //region paging
    @Query("SELECT * FROM ${SettingUser.ENTITY_NAME} ORDER BY name")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SettingUser>
    //endregion

    @Query("SELECT * FROM ${SettingUser.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SettingUser?

    @Query("SELECT * FROM ${SettingUser.ENTITY_NAME} ORDER BY name")
    abstract override fun viewAll(): LiveData<List<SettingUser>>?

    @Query("SELECT * FROM ${SettingUser.ENTITY_NAME} ORDER BY name")
    abstract fun viewAllSimpleList(): List<SettingUser>?

    @Query("UPDATE ${SettingUser.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT name FROM ${SettingUser.ENTITY_NAME} GROUP BY name ORDER BY name")
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SettingUser.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}