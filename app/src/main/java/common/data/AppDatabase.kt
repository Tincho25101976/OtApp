package com.vsg.agendaandpublication.common.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vsg.helper.common.cast.Convert
import common.data.convert.ConvertCurrentModel
import common.model.master.batch.MasterBatch
import common.model.master.batch.MasterBatchDao
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyDao
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemDao
import common.model.master.section.MasterSection
import common.model.master.section.MasterSectionDao
import common.model.master.stock.MasterStock
import common.model.master.stock.MasterStockDao
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.MasterWarehouseDao
import common.model.securityDialog.xact.Xact
import common.model.securityDialog.xact.XactDao

@Database(
    entities =
    [
        MasterCompany::class, MasterItem::class,
        MasterBatch::class, MasterWarehouse::class,
        MasterSection::class, MasterStock::class,

        Xact::class
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
    // master:
    abstract fun companyDao(): MasterCompanyDao
    abstract fun itemDao(): MasterItemDao
    abstract fun batchDao(): MasterBatchDao
    abstract fun warehouseDao(): MasterWarehouseDao
    abstract fun sectionDao(): MasterSectionDao
    abstract fun stockDao(): MasterStockDao

    // securityDialog:
    abstract fun xactDao(): XactDao
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