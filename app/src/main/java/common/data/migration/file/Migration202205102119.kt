package com.vsg.ot.common.data.migration.file

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vsg.ot.common.model.setting.menu.SettingMenu
import com.vsg.ot.common.model.setting.profile.SettingProfile
import com.vsg.ot.common.model.setting.user.SettingUser
import common.data.migration.util.MigrationUtil

class Migration202205102119 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        var sqlCreate: String = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                "'name' TEXT NOT NULL \n" +
                "'planta' INTEGER NOT NULL \n" +
                "'mail' TEXT NOT NULL \n" +
                ")"

        MigrationUtil(database, SettingUser.ENTITY_NAME).run {
            drop()
            if (!exist()) create(sqlCreate)
        }

        sqlCreate = "(" +
        "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                ")"

        MigrationUtil(database, SettingMenu.ENTITY_NAME).run {
            drop()
            if (!exist()) create(sqlCreate)
        }

        sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                ")"

        MigrationUtil(database, SettingProfile.ENTITY_NAME).run {
            drop()
            if (!exist()) create(sqlCreate)
        }

    }
}