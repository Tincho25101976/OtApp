package com.vsg.agendaandpublication.common.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vsg.helper.common.cast.Convert
import common.data.convert.ConvertCurrentModel
import common.model.master.batch.BatchDao
import common.model.master.batch.MasterBatch
import common.model.master.company.CompanyDao
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.item.ItemDao
import common.model.master.section.MasterSection
import common.model.master.section.SectionDao
import common.model.master.stock.StockDao
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.WarehouseDao

@Database(
    entities =
    [
        MasterCompany::class, MasterItem::class,
        MasterBatch::class, MasterWarehouse::class, MasterSection::class,
    ],
    //views = [
    //    ProvisioningViewRoom::class, TrackingViewRoom::class,
    //    AccountingSummaryViewRoom::class, StockViewRoom::class
    //],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convert::class, ConvertCurrentModel::class)
abstract class AppDatabase : RoomDatabase() {

    //region dataSource
    abstract fun companyDao(): CompanyDao
    abstract fun itemDao(): ItemDao
    abstract fun batchDao(): BatchDao
    abstract fun warehouseDao(): WarehouseDao
    abstract fun sectionDao(): SectionDao
    abstract fun stockDao(): StockDao
    //endregion

    companion object {
        private const val DATABASE_NAME = "dbApp.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        //        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
//                    .addMigrations(Migration_20200222_1218())
                    .build()
            }
            return INSTANCE
        }
    }
}