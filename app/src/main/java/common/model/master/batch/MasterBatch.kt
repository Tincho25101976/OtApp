package common.model.master.batch

import androidx.room.*
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import common.model.master.item.MasterItem
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
            entity = Company::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idCompany"),
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = arrayOf("idProduct", "idCompany"), name = "IX_BATCH_FK"),
        Index(value = arrayOf("createDate"), name = "IX_BATCH_CREATE"),
        Index(value = arrayOf("receiverQty", "createDate", "dueDate"), name = "IX_BATCH_ITEMS")],
    inheritSuperIndices = true,
    tableName = MasterBatch.ENTITY_NAME
)
class MasterBatch {
    var receiverQty: Double = 0.0
    var dueDate: Date? = null

    @Ignore
    var quantity: Double = 0.0
    val absolute: Double get() = abs(quantity)

    //region fk
    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idProduct: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    var product: MasterItem? = null

    //endregion

    //region for add
    fun isOKForAdd(): Boolean =
        company != null && money != null && person != null && section != null

    fun itemForAdd(quantity: Double): StockDTO? {
        return when (isOKForAdd()) {
            true -> StockDTO(this, section = section, quantity = quantity)
            false -> null
        }
    }
    //endregion

    //region util
    val percentUsefulLife: Double
        get() {
            return try {
                if (product == null) return 0.0
                if (product!!.shellLife <= 0) return 0.0
                if (dueDate == null || createDate == null) return 0.0
                val rango: Long = dueDate.toPeriod(createDate)
                if (rango <= 0) return 0.0
                ((rango * 100) / product!!.shellLife).toDouble()
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
            if (dueDate == null || product == null) return TypeBatchStatus.UNDEFINED
            return try {
                val fecha: Date = HelperDate.now()
                if (fecha >= dueDate) TypeBatchStatus.EXPIRED
                if (product!!.shellLifeAlert <= 0) TypeBatchStatus.RIGHT
                val rango: Long = dueDate.toPeriod(fecha)
                if (product!!.shellLifeAlert >= rango) TypeBatchStatus.NEAR_EXPIRY
                TypeBatchStatus.RIGHT
            } catch (e: Exception) {
                TypeBatchStatus.UNDEFINED
            }
        }

    fun setDueDate(product: Product) {
        this.dueDate = createDate.addDay(product.shellLife)
    }
    //endregion

    //region description
    override val title: String
        get() = valueCode

    override fun getDrawableShow(): Int = when (this.isEnabled) {
        true -> R.drawable.pic_batch
        false -> R.drawable.pic_batch_grayscale
    }

    override fun reference(): Spanned {
        val sb = StringBuilder().append(spannedData())
        return sb.castToHtml()
    }

    override fun descriptionView(): Spanned {
        val sb = StringBuilder().append(spannedData())
        sb.getBaseDescriptionView()
        return sb.castToHtml()
    }

    private fun spannedData(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(codename)
        sb.toLineSpanned("Recepción", receiverQty.toFormat())
        sb.toLineSpanned("Creación", createDate.toDateString(FormatDateString.CREATE_DATE))
        sb.toLineSpanned("Vencimiento", dueDate.toDateString(FormatDateString.CREATE_DATE))
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is Batch) return false
        return dueDate?.time == other.dueDate?.time
                && receiverQty == other.receiverQty
                && idProduct == other.idProduct
    }
    //endregion

    companion object {
        const val ENTITY_NAME: String = "batch"
    }
}