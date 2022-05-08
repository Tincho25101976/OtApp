package com.vsg.ot.common.model.securityDialog.xact.record

import android.graphics.Bitmap
import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toOneLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import common.model.init.entity.EntityOt
import common.model.master.item.type.TypePlant
import java.util.*

@Entity(
    indices = [
        Index(
            value = arrayOf("caption", "description", "idEvent", "idSector", "planta"),
            name = "IX_XACT"
        )],
    inheritSuperIndices = true,
    tableName = XactRecord.ENTITY_NAME
)
class XactRecord : EntityOt<XactRecord>() {
    //region properties
    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    var picture: ByteArray? = null
    var planta: TypePlant = TypePlant.UNDEFINED
    var updateDate: Date? = null
    var caption: String = ""

    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idEvent: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    var event: XactEvent? = null

    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idSector: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var sector: XactSector? = null
    //endregion

    // recycler adapter
    override val isBitmap: Boolean
        get() = true
    override val title: String
        get() = this.caption

    override fun oGetPictureShow(): ByteArray? {
        return this.picture
    }

    override fun descriptionSpanned(): Spanned {
        val sb = StringBuilder()
        sb.toLineSpanned("Planta", planta.title)
        if (event != null) sb.toLineSpanned("Evento", event?.valueCode)
        if (sector != null) sb.toLineSpanned("Sector", sector?.valueCode)
        sb.toLineSpanned("Observación", description, true)
        return sb.castToHtml()
    }
    //endregion

    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_xact_record

    override fun oGetSpannedGeneric(): StringBuilder {
        val sb = StringBuilder()
        sb.toTitleSpanned(caption)
        sb.toLineSpanned("Planta", planta.title)
        if (event != null) sb.toLineSpanned("Evento", event?.valueCode)
        if (sector != null) sb.toLineSpanned("Sector", sector?.valueCode)
        return sb
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is XactRecord) return false
        return description == other.description
                && picture.contentEquals(other.picture)
                && planta == other.planta
                && caption == other.caption
                && createDate.time == other.createDate.time
                && updateDate?.time == other.updateDate?.time
                && idSector == other.idSector
                && idEvent == other.idEvent
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXactRecord"
    }
}