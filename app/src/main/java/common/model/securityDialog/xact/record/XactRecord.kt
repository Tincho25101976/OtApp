package com.vsg.ot.common.model.securityDialog.xact.xact

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.process.XactProcess
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import common.model.init.entity.EntityOt
import common.model.master.item.type.TypePlant
import java.util.*

@Entity(
    indices = [
        Index(
            value = arrayOf("caption", "description", "evento"),
            name = "IX_XACT"
        )],
    inheritSuperIndices = true,
    tableName = XactRecord.ENTITY_NAME
)
class XactRecord : EntityOt<XactRecord>() {
    //region properties
    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    var picture: ByteArray? = null
    var evento: String = ""
    var equipo: String = ""
    var dob: String = ""
    var planta: TypePlant = TypePlant.UNDEFINED
    var updateDate: Date? = null
    var caption: String = ""

    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idProcess: Int = 0
    @EntityForeignKeyID(10)
    @Ignore
    var process: XactProcess? = null

    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idSector: Int = 0
    @EntityForeignKeyID(20)
    @Ignore
    var sector: XactSector? = null
    //endregion

    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_default

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is XactRecord) return false
        return description == other.description
                && picture.contentEquals(other.picture)
                && evento == other.evento
                && equipo == other.equipo
                && dob == other.dob
                && planta == other.planta
                && caption == other.caption
                && updateDate?.time == other.updateDate?.time
                && idSector == other.idSector
                && idProcess == other.idProcess
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXactRecord"
    }
}