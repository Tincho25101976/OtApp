package com.vsg.ot.common.data.migration.file

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vsg.ot.common.model.securityDialog.security.group.SecurityGroup
import com.vsg.ot.common.model.securityDialog.security.item.SecurityItem
import com.vsg.ot.common.model.securityDialog.security.process.SecurityProcess
import com.vsg.ot.common.model.securityDialog.security.reference.SecurityReference
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRating
import common.data.migration.util.MigrationUtil

class Migration202205102119 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        var sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                ")"

        MigrationUtil(database, XactRating.ENTITY_NAME).apply {
            create(sqlCreate)
            createIndex(
                "IX_XACT_RATING",
                XactRating.ENTITY_NAME,
                "'valueCode', 'description'"
            )
        }

        sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                ")"

        MigrationUtil(database, SecurityReference.ENTITY_NAME).apply {
            create(sqlCreate)
            createIndex(
                "IX_SECURITY_REFERENCE",
                SecurityReference.ENTITY_NAME,
                "'valueCode', 'description'"
            )
        }

        sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL, \n" +
                "'shift' INTEGER NOT NULL, \n" +
                "'time' INTEGER NOT NULL \n" +
                "'contactada' INTEGER NOT NULL \n" +
                "'observada' INTEGER NOT NULL \n" +
                "'actoSeguro' TEXT NOT NULL \n" +
                "'actoInseguro' TEXT NOT NULL \n" +
                "'medidaCorrectiva' TEXT NOT NULL \n" +
                "'planta' INTEGER NOT NULL \n" +
                "'idSector' INTEGER NOT NULL \n" +
                "'idReference' INTEGER NOT NULL \n" +
                ")"

        MigrationUtil(database, SecurityProcess.ENTITY_NAME).apply {
            create(sqlCreate)
            createIndex(
                "IX_SECURITY_PROCESS_FK",
                SecurityProcess.ENTITY_NAME,
                "'idSector', 'idReference', 'createDate'"
            )
            createIndex(
                "IX_SECURITY_PROCESS",
                SecurityProcess.ENTITY_NAME,
                "'valueCode', 'description'"
            )
        }

        sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                "'idProcess' INTEGER NOT NULL, \n" +
                ")"

        MigrationUtil(database, SecurityGroup.ENTITY_NAME).apply {
            create(sqlCreate)
            createIndex(
                "IX_SECURITY_GROUP",
                SecurityGroup.ENTITY_NAME,
                "'valueCode', 'description', 'idProcess'"
            )
        }

        sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL, \n" +
                "'value' INTEGER NOT NULL, \n" +
                "'idGroup' INTEGER NOT NULL \n" +
                ")"

        MigrationUtil(database, SecurityItem.ENTITY_NAME).run {
            create(sqlCreate)
            createIndex(
                "IX_SECURITY_ITEM",
                SecurityItem.ENTITY_NAME,
                "'valueCode', 'description', 'value', 'idGroup'"
            )
        }


    }
}