package com.vsg.ot.common.model.securityDialog.xact.process

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
            name = "IX_XACT_PROCESS"
        )],
    inheritSuperIndices = true,
    tableName = XactProcess.ENTITY_NAME
)
class XactProcess : EntityOt<XactProcess>() {
    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_process

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is XactProcess) return false
        return valueCode == other.valueCode
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXactProcess"
    }
}