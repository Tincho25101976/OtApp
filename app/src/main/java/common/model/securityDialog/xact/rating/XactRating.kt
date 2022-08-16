package com.vsg.ot.common.model.securityDialog.xact.rating

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.common.export.ExportGenericEntityItem
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.init.entity.EntityOtParseWithExport

@Entity(
    indices = [
        Index(
            value = arrayOf("valueCode", "description"),
            name = "IX_XACT_RATING"
        )],
    inheritSuperIndices = true,
    tableName = XactRating.ENTITY_NAME
)
class XactRating: EntityOtParseWithExport<XactRating>() {

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_xact_sector_update

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is XactRating) return false
        return valueCode == other.valueCode
                && description == other.description
    }

    override fun getFields(): List<String> {
        return listOf("id", "valueCode", "description", "isDefault", "isEnabled")
    }

    override fun aGetExportItem(): List<ExportGenericEntityItem<*>> {
        return listOf<ExportGenericEntityItem<out Any>>(
            ExportGenericEntityItem("id", value = id),
            ExportGenericEntityItem("valueCode", value = valueCode),
            ExportGenericEntityItem("description", value = description),
            ExportGenericEntityItem("isDefault", value = isDefault),
            ExportGenericEntityItem("isEnabled", value = isEnabled)
        )
    }

    override fun aGetItemCast(): XactRating = XactRating()
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXactRating"
    }
}