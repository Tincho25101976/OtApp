package com.vsg.ot.common.model.setting.menu

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOt

@Dao
abstract class SettingMenuDao : DaoGenericOt<SettingMenu>() {
    //region paging
    @Query("SELECT * FROM ${SettingMenu.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SettingMenu>
    //endregion

    @Query("SELECT * FROM ${SettingMenu.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SettingMenu?

    @Query("SELECT * FROM ${SettingMenu.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<SettingMenu>>?

    @Query("SELECT * FROM ${SettingMenu.ENTITY_NAME} ORDER BY description")
    abstract fun viewAllSimpleList(): List<SettingMenu>?

    @Query("UPDATE ${SettingMenu.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT description FROM ${SettingMenu.ENTITY_NAME} GROUP BY description ORDER BY description")
    abstract fun viewAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SettingMenu.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}