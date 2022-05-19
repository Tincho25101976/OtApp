package com.vsg.ot.common.model.setting.profile.menu

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.vsg.ot.common.model.setting.menu.SettingMenu
import com.vsg.ot.common.model.setting.profile.SettingProfile
import common.model.init.dao.DaoGenericOt

@Dao
abstract class SettingProfileMenuDao : DaoGenericOt<SettingProfileMenu>() {
    //region paging
    @Query("SELECT * FROM ${SettingProfileMenu.ENTITY_NAME} ORDER BY idProfile")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, SettingProfileMenu>
    //endregion

    @Query("SELECT * FROM ${SettingProfileMenu.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): SettingProfileMenu?

    @Query("SELECT * FROM ${SettingProfileMenu.ENTITY_NAME} ORDER BY idProfile")
    abstract override fun viewAll(): LiveData<List<SettingProfileMenu>>?

    @Query("SELECT * FROM ${SettingProfileMenu.ENTITY_NAME} ORDER BY idProfile")
    abstract fun viewAllSimpleList(): List<SettingProfileMenu>?

    @Query("UPDATE ${SettingProfileMenu.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query(
        "SELECT sp.description " +
                "FROM ${SettingProfileMenu.ENTITY_NAME} e " +
                "INNER JOIN ${SettingProfile.ENTITY_NAME} sp " +
                "   ON sp.id = e.idProfile " +
                "INNER JOIN ${SettingMenu.ENTITY_NAME} sm " +
                "   ON sm.id = e.idMenu " +
                "GROUP BY e.idProfile ORDER BY sp.description"
    )
    abstract override fun viewGetAllTextSearch(): LiveData<List<String>>

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${SettingProfileMenu.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion
}