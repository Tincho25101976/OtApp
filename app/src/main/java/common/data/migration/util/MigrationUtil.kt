package common.data.migration.util

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

class MigrationUtil(private val db: SupportSQLiteDatabase, val tableName: String) {

    private val tableNameTemp = "${tableName}_temp_${Calendar.getInstance().timeInMillis}"

    fun migrateUtil(sqlCreate: String) {
//        // 1. Create new table
        val exist = create(sqlCreate)

        if (exist) {
            // 2. Copy the data
            insertInto()

            // 3. Remove the old table
            drop()

            // 4. Change the table name to the correct one
            renameTable()
        }
    }

    fun createIndex(indexName: String, tableName: String = this.tableName, fields: String) {
        if(!existIndex(indexName)) db.execSQL("CREATE INDEX $indexName ON $tableName ($fields)")
    }

    fun create(sqlCreate: String): Boolean {
        val exist = existTable()
        val sqlCreateFull = "CREATE TABLE IF NOT EXISTS ${
            when (exist) {
                true -> "'$tableNameTemp'"
                false -> "'$tableName'"
            }
        } " + sqlCreate

        // 1. Create new table
        db.execSQL(sqlCreateFull)
        return exist
    }

    fun insertInto(tableNameTemp: String = this.tableNameTemp, tableName: String = this.tableName) {
        db.execSQL("INSERT INTO $tableNameTemp SELECT * FROM $tableName")
    }

    fun drop(tableName: String = this.tableName) {
        db.execSQL("DROP TABLE IF EXISTS $tableName")
    }

    fun existTable(tableName: String = this.tableName): Boolean {
        val result: Boolean
        try {
            var count = 0
            val nameIndex = "COUNT"
            val query =
                "SELECT COUNT(name) AS $nameIndex FROM sqlite_master WHERE type='table' AND name='$tableName' GROUP BY name"
            val cursor = db.query(query)
            while (cursor.moveToNext()) {
                val columnIndex: Int = cursor.getColumnIndex(nameIndex)
                count = cursor.getInt(columnIndex)
            }
            result = count > 0
        } catch (ex: Exception) {
            return false
        }
        return result
    }
    fun existIndex(indexName: String): Boolean {
        val result: Boolean
        try {
            var count = 0
            val nameIndex = "COUNT"
            val query =
                "SELECT COUNT(name) AS $nameIndex FROM sqlite_master WHERE type='index' AND name='$indexName' GROUP BY name"
            val cursor = db.query(query)
            while (cursor.moveToNext()) {
                val columnIndex: Int = cursor.getColumnIndex(nameIndex)
                count = cursor.getInt(columnIndex)
            }
            result = count > 0
        } catch (ex: Exception) {
            return false
        }
        return result
    }

    fun renameTable(
        tableNameTemp: String = this.tableNameTemp,
        tableName: String = this.tableName
    ) {
        db.execSQL("ALTER TABLE $tableNameTemp RENAME TO $tableName")
    }

    fun addColumn(column: String, typeColumn: String, tableName: String = this.tableName) {
        //ALTER TABLE TABLE_NAME ADD COLUMN COLUMN_NAME COLUMN_TYPE;
        db.execSQL("ALTER TABLE $tableName ADD COLUMN '$column' $typeColumn")
    }

    fun renameColumn(oldName: String, newName: String, tableName: String = this.tableName) {
        db.execSQL("ALTER TABLE $tableName RENAME COLUMN '$oldName' TO '$newName';")
    }

    fun delete(tableName: String = this.tableName) {
        db.execSQL("DELETE FROM $tableName")
    }

    fun execute(sql: String) {
        if (sql.isEmpty()) return
        db.execSQL(sql)
    }

    fun version(): String {
        var result: String = ""
        try {
            val cursor: Cursor = SQLiteDatabase.openOrCreateDatabase(":memory:", null)
                .rawQuery("select sqlite_version() AS sqlite_version", null)
            while (cursor.moveToNext()) {
                result += cursor.getString(0)
            }
        } catch (ex: Exception) {
            return "Error en la Query"
        }
        return result
    }
}