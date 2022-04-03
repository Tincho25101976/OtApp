package common.model.securityDialog.xact

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.init.entity.EntityOt
import java.util.*

@Entity(
    indices = [
        Index(
            value = arrayOf("caption", "description", "evento"),
            name = "IX_XACT"
        )],
    inheritSuperIndices = true,
    tableName = Xact.ENTITY_NAME
)
class Xact : EntityOt<Xact>() {
    //region properties
    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    var picture: ByteArray? = null
    var evento: String = ""
    var equipo: String = ""
    var dob: String = ""
    var planta: String = ""
    var addedTime: Date? = null
    var updatedTime: Date? = null
    var caption: String = ""
    //endregion

    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_default

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is Xact) return false
        return description == other.description
                && picture.contentEquals(other.picture)
                && evento == other.evento
                && equipo == other.equipo
                && dob == other.dob
                && planta == other.planta
                && caption == other.caption
                && addedTime?.time == other.addedTime?.time
                && updatedTime?.time == other.updatedTime?.time
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXact"
    }
}