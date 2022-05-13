package com.vsg.ot.common.data.migration.file

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import common.data.migration.util.MigrationUtil

class Migration202204262051: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        var sqlCreate: String = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                ")"
        MigrationUtil(database, XactEvent.ENTITY_NAME).run {
            drop()
            if(!existTable()) create(sqlCreate)
        }

        sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL \n" +
                ")"
        MigrationUtil(database, XactSector.ENTITY_NAME).run {
            drop()
            if(!existTable()) create(sqlCreate)
        }

        sqlCreate = "(" +
                "'valueCode' TEXT NOT NULL, \n" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "'isDefault' INTEGER NOT NULL, \n" +
                "'isEnabled' INTEGER NOT NULL, \n" +
                "'createDate' INTEGER NOT NULL, \n" +
                "'description' TEXT NOT NULL, \n" +

                "'picture' BLOB NOT NULL, \n" +
                "'planta' INTEGER NOT NULL, \n" +
                "'caption' TEXT NOT NULL, \n" +
                "'updateDate' INTEGER NOT NULL, \n" +
                "'idEvent' INTEGER NOT NULL, \n" +
                "'idSector' INTEGER NOT NULL \n" +
                "FOREIGN KEY ('idEvent') REFERENCES ${XactEvent.ENTITY_NAME}('id') ON UPDATE NO ACTION ON DELETE CASCADE" +
                "FOREIGN KEY ('idSector') REFERENCES ${XactSector.ENTITY_NAME}('id') ON UPDATE NO ACTION ON DELETE CASCADE" +
                ")"

        MigrationUtil(database, XactRecord.ENTITY_NAME).run {
            drop()
            if(!existTable()) create(sqlCreate)
        }
    }
}