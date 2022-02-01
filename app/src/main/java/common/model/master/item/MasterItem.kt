package common.model.master.item

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.master.unit.TypeUnit
import common.helper.HelperMaster.Companion.toMasterUnit
import common.model.ItemOtBaseCompany
import common.model.master.company.MasterCompany
import common.model.master.unit.MasterUnit

@Entity(
    foreignKeys =
    [
        ForeignKey(
            entity = MasterCompany::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idCompany"),
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = arrayOf("id", "idCompany"), name = "IX_ITEM_FK"),
        Index(value = arrayOf("id", "item", "unit"), name = "IX_ITEM")],
    inheritSuperIndices = true,
    tableName = MasterItem.ENTITY_NAME
)
class MasterItem : ItemOtBaseCompany<MasterItem>() {
    //region properties
    var item: String = ""
    var shellLife: Int = 0
    var shellLifeAlert: Int = 0
    var unit: MasterUnit? = null
    var typeProduct: TypeProduct = TypeProduct.UNDEFINED
    var typePlant: TypePlant = TypePlant.UNDEFINED
    override val title: String
        get() = item
    //endregion

    @Ignore
    override fun oGetDrawablePicture(): Int = R.drawable.pic_product
    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(item)
        sb.toLineSpanned("Producto", description)
        if (unit != null) sb.toLineSpanned("Unidad", unit?.symbol)
        sb.toLineSpanned("Vida útil", shellLife)
        sb.toLineSpanned("Tipo de producto", typeProduct.title)
        sb.toLineSpanned("Planta", typePlant.title)
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterItem) return false
        return item == other.item
                && description == other.description
                && unit == other.unit
    }
    //endregion

    //region function
    fun setUnit(unit: TypeUnit, precision: Int = 3) {
        this.unit = unit.toMasterUnit(precision)
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterItem"
    }
}