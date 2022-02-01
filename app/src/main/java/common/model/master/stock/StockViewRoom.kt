package common.model.master.stock

import androidx.room.DatabaseView
import androidx.room.Ignore
import common.model.master.section.TypeSection
import com.vsg.helper.common.model.IEntity
import common.helper.HelperMaster.Companion.toMasterUnit
import common.model.master.batch.MasterBatch
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
import common.model.master.unit.MasterUnit
import common.model.master.unit.TypeUnit
import common.model.master.warehouse.MasterWarehouse
import java.util.*

@DatabaseView(
    "SELECT" +
            "  e.idCompany, e.companyName," +
            "  e.idOperation," +
            "  e.idBatch, e.batchValueCode, e.batchCreateDate, e.batchReceiverQty, e.batchDueDate," +
            "  e.idProduct, e.productName, e.productStockMin, e.productStockMax, e.productValueCode," +
            "  e.idUnit, e.unitName, e.unitSymbol, e.unitDecimals," +
            "  e.idCategory, e.categoryName," +
            "  e.idSection, e.sectionName, e.sectionType, e.sectionValueCode," +
            "  e.idWarehouse, e.warehouseName, e.warehouseLocation, e.warehouseValueCode," +
            "  e.idMoney, e.moneyType, e.moneyValueCode," +
            "  e.quantity " +
            "FROM " +
            "  (" +
            "  SELECT " +
            "    t.idCompany, com.name as companyName," +
            "    t.idBatch, b.valueCode as batchValueCode, b.createDate as batchCreateDate," +
            "    b.receiverQty as batchReceiverQty, b.dueDate as batchDueDate," +
            "    t.idProduct, p.name as productName, p.stockMin as productStockMin," +
            "    p.stockMax as productStockMax, p.valueCode as productValueCode," +
            "    p.idUnit, u.name as unitName, u.symbol as unitSymbol, u.decimals as unitDecimals," +
            "    t.idSection, s.name as sectionName, s.type as sectionType, s.valueCode as sectionValueCode," +
            "    s.idWarehouse, w.name as warehouseName, w.location as warehouseLocation, w.valueCode as warehouseValueCode," +
            "    m.id as idMoney, m.type as moneyType, m.valueCode as moneyValueCode," +
            "    sum(CASE t.typeMovement" +
            "      WHEN 1 then t.quantity" +
            "      WHEN 2 then t.quantity * -1" +
            "      ELSE 0" +
            "    END) as quantity " +
            "  FROM " +
            "    transactions t" +
            "    INNER JOIN ${MasterBatch.ENTITY_NAME} b ON b.id = t.idBatch AND b.idCompany = t.idCompany" +
            "    INNER JOIN ${MasterSection.ENTITY_NAME} s ON s.id = t.idSection AND s.idCompany = t.idCompany" +
            "    INNER JOIN ${MasterWarehouse.ENTITY_NAME} w ON w.id = s.idWarehouse AND w.idCompany = s.idCompany" +
            "    INNER JOIN ${MasterItem.ENTITY_NAME} p ON p.id = t.idProduct AND s.idCompany = t.idCompany" +
            "    INNER JOIN ${MasterCompany.ENTITY_NAME} com ON com.id = t.idCompany" +
            "  WHERE" +
            "    p.isEnabled = 1 AND p.hasStock = 1 " +
            "  GROUP BY" +
            "    t.idCompany, com.name," +
            "    od.idOperation," +
            "    t.idBatch, b.valueCode, b.createDate, b.receiverQty, b.dueDate," +
            "    t.idProduct, p.name, p.stockMin, p.stockMax, p.valueCode," +
            "    p.idUnit, u.name, u.symbol, u.decimals," +
            "    p.idCategory, c.name," +
            "    t.idSection, s.name, s.type, s.valueCode," +
            "    s.idWarehouse, w.name, w.location, w.valueCode," +
            "    m.id, m.type, m.valueCode," +
            "    t.typeMovement" +
            "  ) e " +
            "WHERE" +
            "  e.quantity != 0 ;",
    viewName = StockViewRoom.ENTITY_NAME
)
class StockViewRoom : IEntity {
    @Ignore
    override var id: Int = 0
    var idCompany: Int = 0
    var companyName: String = ""
    var idBatch: Int = 0
    var batchValueCode: String = ""
    var batchCreateDate: Date? = null
    var batchReceiverQty: Double = 0.0
    var batchDueDate: Date? = null
    var idProduct: Int = 0
    var productName: String = ""
    var productValueCode: String = ""
    var idUnit: TypeUnit = TypeUnit.UNDEFINED
    var unitName: String = ""
    var unitSymbol: String = ""
    var unitDecimals: Int = 0
    var idSection: Int = 0
    var sectionName: String = ""
    var sectionType: TypeSection = TypeSection.UNDEFINED
    var sectionValueCode: String = ""
    var idWarehouse: Int = 0
    var warehouseName: String = ""
    var warehouseLocation: String = ""
    var warehouseValueCode: String = ""
    var quantity: Double = 0.0

    val batch: MasterBatch?
        get() = try {
            MasterBatch().apply {
                id = idBatch
                createDate = batchCreateDate
                dueDate = batchDueDate
                receiverQty = batchReceiverQty
                valueCode = batchValueCode
                quantity = this@StockViewRoom.quantity
                idItem = this@StockViewRoom.idProduct
                idCompany = this@StockViewRoom.idCompany
                company = this@StockViewRoom.company
                item = this@StockViewRoom.product
                section = this@StockViewRoom.section
            }
        } catch (ex: Exception) {
            null
        }
    val product: MasterItem?
        get() = try {
            MasterItem().apply {
                id = idProduct
                description = productName
                valueCode = productValueCode
                idCompany = this@StockViewRoom.idCompany
                company = this@StockViewRoom.company
                unit = this@StockViewRoom.unit
            }
        } catch (ex: Exception) {
            null
        }
    val unit: MasterUnit?
        get() = try {
            this.idUnit.toMasterUnit()
        } catch (ex: Exception) {
            null
        }
    val section: MasterSection?
        get() = try {
            MasterSection().apply {
                id = idSection
                description = sectionName
                type = sectionType
                valueCode = sectionValueCode
                idCompany = this@StockViewRoom.idCompany
                company = this@StockViewRoom.company
                idWarehouse = this@StockViewRoom.idWarehouse
                warehouse = this@StockViewRoom.warehouse
            }
        } catch (ex: Exception) {
            null
        }
    val warehouse: MasterWarehouse?
        get() = try {
            MasterWarehouse().apply {
                id = idWarehouse
                description = warehouseName
                location = warehouseLocation
                valueCode = warehouseValueCode
                idCompany = this@StockViewRoom.idCompany
                company = this@StockViewRoom.company
            }
        } catch (ex: Exception) {
            null
        }
    val company: MasterCompany?
        get() = try {
            MasterCompany().apply {
                id = idCompany
                description = companyName
            }
        } catch (ex: Exception) {
            null
        }
    val stock: MasterStockDTO?
        get() {
            var data: MasterStockDTO? = null
            val batchProduct: MasterBatch? = batch
            if (batchProduct != null) {
                batchProduct.quantity = this.quantity
                data = MasterStockDTO(batchProduct).apply {
                    section = this@StockViewRoom.section
                }
            }
            return data
        }

    companion object {
        const val ENTITY_NAME: String = "stockViewRoom"
    }
}