package common.model.master.section

import androidx.room.*
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.init.entity.EntityOtCompany
import common.model.master.company.MasterCompany
import common.model.master.section.type.TypeSection
import common.model.master.warehouse.MasterWarehouse

@Entity(
    foreignKeys =
    [
        ForeignKey(
            entity = MasterWarehouse::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idWarehouse"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MasterCompany::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idCompany"),
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices =
    [
        Index(value = arrayOf("id", "idWarehouse", "idCompany"), name = "IX_SECTION_FK"),
        Index(value = arrayOf("valueCode", "isEnabled", "type"), name = "IX_SECTION_MAIN"),
    ],
    inheritSuperIndices = true,
    tableName = MasterSection.ENTITY_NAME
)
class MasterSection : EntityOtCompany<MasterSection>() {

    var type: TypeSection = TypeSection.NORMAL

    // region fk
    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idWarehouse: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var warehouse: MasterWarehouse? = null
    //endregion


    //region reference
    override fun oGetDrawablePicture(): Int = R.drawable.pic_section

    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(codename)
        sb.toLineSpanned("Abreviatura", prefix)
        sb.toLineSpanned("Tipo", type.title)
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterSection) return false
        return type == other.type
                && description == other.description
                && createDate.time == other.createDate.time
    }
    //endregion

    companion object {
        const val ENTITY_NAME: String = "masterSection"
        const val DEFAULT_PREFIX: String = "SW"
    }
}