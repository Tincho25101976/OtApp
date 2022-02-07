package common.model.master.company

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.common.model.IEntityPicture
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.init.entity.EntityOt
import common.model.master.batch.MasterBatch
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
import common.model.master.stock.MasterStock
import common.model.master.warehouse.MasterWarehouse

@Entity(tableName = MasterCompany.ENTITY_NAME)
class MasterCompany : EntityOt<MasterCompany>(), IEntityPicture {

    //region properties
    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    override var picture: ByteArray? = null

    @Ignore
    val masterItem: MutableList<MasterItem> = mutableListOf()

    @Ignore
    val masterBatch: MutableList<MasterBatch> = mutableListOf()

    @Ignore
    val masterWarehouse: MutableList<MasterWarehouse> = mutableListOf()

    @Ignore
    val masterSection: MutableList<MasterSection> = mutableListOf()

    @Ignore
    val masterStock: MutableList<MasterStock> = mutableListOf()

    //endregion

    //region methods
    @Ignore
    override fun oGetPictureShow(): ByteArray? = this.picture

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_company

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterCompany) return false
        return valueCode == other.valueCode
                && description == other.description
                && picture.contentEquals(other.picture)
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "company"
    }
}