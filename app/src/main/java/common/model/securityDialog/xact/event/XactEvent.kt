package com.vsg.ot.common.model.securityDialog.xact.event

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import common.model.init.entity.EntityOt

@Entity(
    indices = [
        Index(
            value = arrayOf("valueCode", "description"),
            name = "IX_XACT_EVENT"
        )],
    inheritSuperIndices = true,
    tableName = XactEvent.ENTITY_NAME
)
class XactEvent : EntityOt<XactEvent>() {
    //region properties
    @Ignore
    public var records: List<XactRecord>? = null
    //endregion

    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_xact_event

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is XactEvent) return false
        return valueCode == other.valueCode
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXactEvent"
    }
}