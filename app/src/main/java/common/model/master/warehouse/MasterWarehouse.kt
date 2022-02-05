package common.model.master.warehouse

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.common.model.EntityForeignKeyList
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.init.entity.EntityOtCompany
import common.model.master.company.MasterCompany
import common.model.master.section.MasterSection

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
        Index(value = arrayOf("idCompany"), name = "IX_WAREHOUSE_FK"),
        Index(value = arrayOf("description", "prefix", "isEnabled"), name = "IX_WAREHOUSE_MAIN"),
    ],
    inheritSuperIndices = true,
    tableName = MasterWarehouse.ENTITY_NAME
)
class MasterWarehouse : EntityOtCompany<MasterWarehouse>() {
    var location: String = ""

    @EntityForeignKeyList
    @Ignore
    val section: MutableList<MasterSection> = mutableListOf()

    //region reference
    override fun oGetDrawablePicture(): Int = R.drawable.pic_warehouse
    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(valueCode)
        if (location.isNotEmpty()) sb.toLineSpanned("Ubicación", location)
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterWarehouse) return false
        return description == other.description
                && location == other.location
                && valueCode == other.valueCode
    }
    //endregion

    companion object {
        const val ENTITY_NAME: String = "warehouse"
        const val DEFAULT_PREFIX: String = "W"
    }
}