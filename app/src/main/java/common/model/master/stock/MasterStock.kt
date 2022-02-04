package common.model.master.stock

import androidx.room.*
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.init.entity.EntityOtCompany
import common.model.master.batch.MasterBatch
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
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
        Index(value = arrayOf("idProduct", "idCompany"), name = "IX_BATCH_FK"),
        Index(value = arrayOf("createDate"), name = "IX_BATCH_CREATE"),
        Index(value = arrayOf("receiverQty", "createDate", "dueDate"), name = "IX_BATCH_ITEMS")],
    inheritSuperIndices = true,
    tableName = MasterBatch.ENTITY_NAME
)
class MasterStock : EntityOtCompany<MasterStock>() {

    //region properties
    var itemCode: String = ""
    var bathCode: String = ""

    @Ignore
    override var valueCode: String = ""
        get() = "$itemCode/$bathCode"
    @Ignore
    var quantity: Double = 0.0
    val absolute: Double get() = abs(quantity)

    override val title: String
        get() = valueCode

    @Ignore
    var section: MasterSection? = null
    //endregion

    //region methods
    @Ignore
    override fun oGetDrawablePicture(): Int = R.drawable.pic_stock

    @Ignore
    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(codename)
        sb.toLineSpanned("Ítem", itemCode)
        sb.toLineSpanned("Batch", bathCode)
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterStock) return false
        return itemCode == other.itemCode
                && bathCode == other.bathCode
                && quantity == other.quantity
    }
    //endregion

    companion object {
        const val ENTITY_NAME: String = "stock"
    }
}