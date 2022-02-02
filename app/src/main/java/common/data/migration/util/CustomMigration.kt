package common.data.migration.util

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

abstract class CustomMigration(version: Int, private val tableName: String) :
    Migration(version, (version + 1)) {
    var onEventMigrate: ((MigrationUtil) -> Unit)? = null
    override fun migrate(database: SupportSQLiteDatabase) {
        onEventMigrate?.invoke(MigrationUtil(database, tableName))
    }
}