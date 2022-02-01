package common.model.master.company

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.common.model.IEntityPicture
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.ItemOtBase

@Entity(tableName = MasterCompany.ENTITY_NAME)
class MasterCompany : ItemOtBase<MasterCompany>(), IEntityPicture {

    //region properties
    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    override var picture: ByteArray? = null
    override val title: String
        get() = description
    //endregion

    //region methods
    @Ignore
    override fun oGetPictureShow(): ByteArray? = this.picture

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_company

    override fun aTitleRecyclerAdapter(): String = "$title ${
        when (hasItems) {
            true -> "*"
            false -> ""
        }
    }"

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterCompany) return false
        return description == other.description
                && picture.contentEquals(other.picture)
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "company"
    }
}