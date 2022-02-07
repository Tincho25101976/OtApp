package common.model.master.item

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.helper.HelperMaster.Companion.toUnit
import common.model.common.unit.Unit
import common.model.common.unit.type.TypeUnit
import common.model.init.entity.EntityOtCompany
import common.model.master.batch.MasterBatch
import common.model.master.company.MasterCompany
import common.model.master.item.type.TypePlant
import common.model.master.item.type.TypeProduct

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
        Index(value = arrayOf("id", "valueCode", "typeUnit"), name = "IX_ITEM")],
    inheritSuperIndices = true,
    tableName = MasterItem.ENTITY_NAME
)
class MasterItem : EntityOtCompany<MasterItem>() {
    //region properties
    var shellLife: Int = 0
    var shellLifeAlert: Int = 0

    @Ignore
    var precision: Int = Unit.DEFAULT_VALUE_PRECISION
        get() = when (field <= 0) {
            true -> Unit.DEFAULT_VALUE_PRECISION
            else -> field
        }
    val unit: Unit get() = typeUnit.toUnit(precision)

    var typeUnit: TypeUnit = TypeUnit.UNDEFINED
    var typeProduct: TypeProduct = TypeProduct.UNDEFINED
    var typePlant: TypePlant = TypePlant.UNDEFINED

    @Ignore
    val masterBatch: MutableList<MasterBatch> = mutableListOf()
    //endregion

    //region methods

    //region implementation
    @Ignore
    override fun oGetDrawablePicture(): Int = R.drawable.pic_product
    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(valueCode)
        sb.toLineSpanned("Producto", description)
        sb.toLineSpanned("Unidad", unit.symbol)
        sb.toLineSpanned("Vida útil", shellLife)
        sb.toLineSpanned("Tipo de producto", typeProduct.title)
        sb.toLineSpanned("Planta", typePlant.title)
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterItem) return false
        return valueCode == other.valueCode
                && description == other.description
                && unit == other.unit
                && typeProduct == other.typeProduct
                && typePlant == other.typePlant
    }
    //endregion

    //endregion

    companion object {
        const val ENTITY_NAME = "masterItem"
    }
}