package com.vsg.ot.common.model.securityDialog.xact.sector

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.init.entity.EntityOt

@Entity(
    indices = [
        Index(
            value = arrayOf("valueCode", "description"),
            name = "IX_XACT_SECTOR"
        )],
    inheritSuperIndices = true,
    tableName = XactSector.ENTITY_NAME
)
class XactSector : EntityOt<XactSector>() {

    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_xact_sector

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is XactSector) return false
        return valueCode == other.valueCode
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXactSector"
    }
}