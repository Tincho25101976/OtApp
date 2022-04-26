package common.model.master.batch

import androidx.room.*
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.date.HelperDate
import com.vsg.helper.helper.date.HelperDate.Companion.addDay
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import com.vsg.helper.helper.date.HelperDate.Companion.toPeriod
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import com.vsg.ot.R
import common.model.init.entity.EntityOtCompany
import common.model.master.batch.type.TypeBatchStatus
import common.model.master.company.MasterCompany
import common.model.master.item.IMasterItem
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
import common.model.master.stock.MasterStock
import common.model.master.warehouse.MasterWarehouse
import java.util.*
import kotlin.math.abs

@Entity(
    foreignKeys =
    [
        ForeignKey(
            entity = MasterItem::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idItem"),
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = MasterCompany::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idCompany"),
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = arrayOf("idItem", "idCompany"), name = "IX_BATCH_FK"),
        Index(value = arrayOf("createDate"), name = "IX_BATCH_CREATE"),
        Index(value = arrayOf("receiverQty", "createDate", "dueDate"), name = "IX_BATCH_ITEMS")],
    inheritSuperIndices = true,
    tableName = MasterBatch.ENTITY_NAME
)
class MasterBatch : EntityOtCompany<MasterBatch>(), IMasterItem {

    //region properties
    var receiverQty: Double = 0.0
    var dueDate: Date? = null

    @Ignore
    var quantity: Double = 0.0

    override val allowDefaultValue: Boolean
        get() = false
    val absolute: Double get() = abs(quantity)
    val percentUsefulLife: Double
        get() {
            return try {
                if (item == null) return 0.0
                if (item!!.shellLife <= 0) return 0.0
                if (dueDate == null || createDate == null) return 0.0
                val rango: Long = dueDate.toPeriod(createDate)
                if (rango <= 0) return 0.0
                ((rango * 100) / item!!.shellLife).toDouble()
            } catch (e: Exception) {
                0.0
            }
        }
    val daysToExpiration: Long
        get() {
            return try {
                createDate.toPeriod(createDate)
            } catch (e: Exception) {
                0
            }
        }
    val status: TypeBatchStatus
        get() {
            if (dueDate == null || item == null) return TypeBatchStatus.UNDEFINED
            return try {
                val fecha: Date = HelperDate.nowDate()
                if (fecha >= dueDate) TypeBatchStatus.EXPIRED
                if (item!!.shellLifeAlert <= 0) TypeBatchStatus.RIGHT
                val rango: Long = dueDate.toPeriod(fecha)
                if (item!!.shellLifeAlert >= rango) TypeBatchStatus.NEAR_EXPIRY
                TypeBatchStatus.RIGHT
            } catch (e: Exception) {
                TypeBatchStatus.UNDEFINED
            }
        }

    @Ignore
    var section: MasterSection? = null

    @Ignore
    var warehouse: MasterWarehouse? = null

    //region fk
    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idItem: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    override var item: MasterItem? = null
    //endregion

    //endregion

    //region methods

    //region for add
    fun isOKForAdd(): Boolean =
        company != null && item != null && warehouse != null && section != null

    fun itemForAdd(quantity: Double): MasterStock? {
        return when (isOKForAdd()) {
            true -> MasterStock().apply {
                item = this@MasterBatch.item!!
                batch = this@MasterBatch
                section = this@MasterBatch.section!!
                warehouse = this@MasterBatch.warehouse!!
                this.quantity = quantity
            }
            false -> null
        }
    }
    //endregion

    //endregion

    //region util
    fun setDueDate(product: MasterItem) {
        this.dueDate = createDate.addDay(product.shellLife)
    }
    //endregion

    //region reference
    @Ignore
    override fun oGetDrawablePicture(): Int = R.drawable.pic_batch

    @Ignore
    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(codename)
        sb.toLineSpanned("Recepción", receiverQty.toFormat())
        sb.toLineSpanned("Creación", createDate.toDateString(FormatDateString.CREATE_DATE))
        sb.toLineSpanned("Vencimiento", dueDate.toDateString(FormatDateString.CREATE_DATE))
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterBatch) return false
        return dueDate?.time == other.dueDate?.time
                && receiverQty == other.receiverQty
                && createDate.time == other.createDate.time
                && idItem == other.idItem
    }
    //endregion

    companion object {
        const val ENTITY_NAME: String = "masterBatch"
    }
}