package com.vsg.ot.common.model.setting.profile.user

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOt

@Dao
abstract class SettingProfileUserDao : DaoGenericOt<SettingProfileUser>() {
    //region paging
    @Query("SELECT * FROM ${SettingProfileUser.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SettingProfileUser>
    //endregion

    @Query("SELECT * FROM ${SettingProfileUser.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SettingProfileUser?

    @Query("SELECT * FROM ${SettingProfileUser.ENTITY_NAME} ORDER BY description")
    abstract override fun viewAll(): LiveData<List<SettingProfileUser>>?

    @Query("SELECT * FROM ${SettingProfileUser.ENTITY_NAME} ORDER BY description")
    abstract fun viewAllSimpleList(): List<SettingProfileUser>?

    @Query("UPDATE ${SettingProfileUser.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT description FROM ${SettingProfileUser.ENTITY_NAME} GROUP BY description ORDER BY description")
    abstract fun viewAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SettingProfileUser.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}