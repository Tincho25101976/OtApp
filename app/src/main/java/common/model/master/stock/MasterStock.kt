package common.model.master.stock

import androidx.room.*
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import com.vsg.ot.R
import common.model.init.entity.EntityOtCompany
import common.model.master.batch.MasterBatch
import common.model.master.batch.type.TypeBatchStatus
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
import common.model.master.warehouse.MasterWarehouse
import kotlin.math.abs

@Entity(
    foreignKeys =
    [
        ForeignKey(
            entity = MasterCompany::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idCompany"),
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = MasterItem::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idItem"),
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = MasterBatch::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idBatch"),
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = MasterWarehouse::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idWarehouse"),
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = MasterSection::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idSection"),
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(
            value = arrayOf("idItem", "idCompany", "idBatch", "idSection", "idWarehouse"),
            name = "IX_STOCK_FK"
        ),
        Index(value = arrayOf("mprController", "materialGrouping"), name = "IX_STOCK_TYPE"),
        Index(value = arrayOf("quantity", "createDate"), name = "IX_STOCK_QTY_DATE")],
    inheritSuperIndices = true,
    tableName = MasterStock.ENTITY_NAME
)
class MasterStock : EntityOtCompany<MasterStock>() {

    //region properties

    //region fk
    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idItem: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var item: MasterItem? = null

    @EntityForeignKeyID(30)
    @ColumnInfo(index = true)
    var idBatch: Int = 0

    @EntityForeignKeyID(30)
    @Ignore
    var batch: MasterBatch? = null

    @EntityForeignKeyID(40)
    @ColumnInfo(index = true)
    var idSection: Int = 0

    @EntityForeignKeyID(40)
    @Ignore
    var section: MasterSection? = null

    @EntityForeignKeyID(50)
    @ColumnInfo(index = true)
    var idWarehouse: Int = 0

    @EntityForeignKeyID(50)
    @Ignore
    var warehouse: MasterWarehouse? = null
    //endregion

    var mprController: String = ""
    var materialGrouping: String = ""
    var quantity: Double = 0.0

    val itemCode: String
        get() {
            return item?.valueCode ?: ""
        }
    val bathCode: String
        get() {
            return when (batch == null) {
                true -> ""
                else -> batch?.valueCode ?: ""
            }
        }
    val warehouseCode: String
        get() {
            return warehouse?.valueCode ?: ""
        }
    val sectionCode: String
        get() {
            return section?.valueCode ?: ""
        }

    val location: String get() = "$warehouseCode/$sectionCode"

    val expirationStatus: TypeBatchStatus get() = batch?.status ?: TypeBatchStatus.UNDEFINED

    @Ignore
    override var valueCode: String = ""
        get() = "$itemCode/$bathCode"

    val absolute: Double get() = abs(quantity)

    override val title: String
        get() = valueCode

    //endregion

    //region methods
    @Ignore
    override fun oGetDrawablePicture(): Int = R.drawable.pic_stock

    @Ignore
    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(codename)
        sb.toLineSpanned("Ítem", itemCode)
        sb.toLineSpanned("Product", item?.description ?: "")
        sb.toLineSpanned("Batch", bathCode)
        sb.toLineSpanned("Warehouse", warehouseCode)
        sb.toLineSpanned("Section", sectionCode)
        sb.toLineSpanned("Qty", quantity.toFormat())
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterStock) return false
        return itemCode == other.itemCode
                && bathCode == other.bathCode
                && idWarehouse == other.idWarehouse
                && idSection == other.idSection
                && quantity == other.quantity
    }
    //endregion

    companion object {
        const val ENTITY_NAME: String = "masterStock"
    }
}