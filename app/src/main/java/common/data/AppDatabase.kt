package com.vsg.ot.common.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vsg.helper.common.cast.Convert
import com.vsg.ot.common.data.migration.file.Migration202204262051
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventDao
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorDao
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
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.xact.XactRecordDao

@Database(
    entities =
    [
        MasterCompany::class, MasterItem::class,
        MasterBatch::class, MasterWarehouse::class,
        MasterSection::class, MasterStock::class,

        XactRecord::class, XactSector::class, XactEvent::class
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
    abstract fun xactRecordDao(): XactRecordDao
    abstract fun xactSectorDao(): XactSectorDao
    abstract fun xactEventDao(): XactEventDao
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
//                    .addMigrations(Migration202204262051())
                    .build()
            }
            return INSTANCE
        }
    }
}