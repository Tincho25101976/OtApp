package common.model.master.stock

import common.model.master.section.TypeSection
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import com.vsg.helper.helper.Helper.Companion.toFormat
import com.vsg.helper.helper.Helper.Companion.toPadStart
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.ItemOtBase
import common.model.master.batch.MasterBatch
import common.model.master.batch.TypeBatchStatus
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
import java.util.*

class MasterStockDTO(
    val batch: MasterBatch,
    var section: MasterSection? = null,
    quantity: Double = 0.0
) : ItemOtBase<MasterStockDTO>(), IEntityViewRoom<MasterStockDTO>
{

    //region init
    init {
        if (quantity > 0.0) batch.quantity = quantity
    }
    //endregion

    //region base
    override val title: String get() = valueCodeBatch
    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterStockDTO) return false
        return product == other.product
                && batch == batch
                && section == section
    }

    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(valueCodeProduct)
        sb.toLineSpanned("Producto", productName)
        sb.toLineSpanned("Ubicación", location)
        sb.toLineSpanned("Cantidad", quantity.toFormat(3))
        sb.toLineSpanned("Vencimiento", dueDate.toDateString(FormatDateString.SIMPLE))
        sb.toLineSpanned("Días para el vencimiento", daysToExpiration.toPadStart())
        return sb
    }

    override fun oGetDrawablePicture(): Int = R.drawable.pic_stock
    //endregion

    val product: MasterItem?
        get() = this.batch.item
    val idBatch: Int
        get() = batch.id
    val idProduct: Int
        get() = batch.idItem
    val idSection: Int
        get() = when (section == null) {
            false -> section!!.id
            true -> 0
        }
    val idWarehouse: Int
        get() = when (section == null) {
            false -> section!!.idWarehouse
            true -> 0
        }

    val unitSymbol: String
        get() = batch.item?.unit?.symbol ?: ""

    val idCompany: Int
        get() = when (batch.idCompany >= 0L) {
            true -> when (section == null) {
                true -> batch.idCompany
                false -> when (batch.idCompany == section!!.idCompany) {
                    true -> section!!.idCompany
                    false -> 0
                }
            }
            false -> 0
        }
    val quantity: Double
        get() = batch.quantity
    override val code: String
        get() = batch.code

    val absolute: Double
        get() = batch.absolute
    val expirationStatus: TypeBatchStatus
        get() = batch.status

    val valueCodeBatch: String
        get() = batch.valueCode
    val valueCodeProduct: String
        get() = batch.item?.valueCode ?: ""
    val productName: String
        get() = product?.description ?: ""
    val sectionName: String
        get() = section?.description ?: ""
    val warehouseName: String
        get() = when (section == null) {
            true -> ""
            false -> when (section?.warehouse == null) {
                true -> ""
                false -> section?.warehouse?.description!!
            }
        }
    val location: String
        get() = when (warehouseName.isEmpty()) {
            true -> sectionName
            false -> "${warehouseName}/${sectionName}"
        }

    val typeSection: TypeSection
        get() = when (section == null) {
            true -> TypeSection.UNDEFINED
            false -> section!!.type
        }

    val dueDate: Date?
        get() = batch.dueDate
    val daysToExpiration: Long
        get() = batch.daysToExpiration
    //endregion
}