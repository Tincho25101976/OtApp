package com.vsg.ot.common.model.securityDialog.security.reference

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
            name = "IX_SECURITY_REFERENCE"
        )],
    inheritSuperIndices = true,
    tableName = SecurityReference.ENTITY_NAME
)
class SecurityReference : EntityOtParseWithExport<SecurityReference>() {

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_security_dialog_reference

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SecurityReference) return false
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

    override fun aGetItemCast(): SecurityReference = SecurityReference()
    //endregion

    companion object {
        const val ENTITY_NAME = "masterSecurityReference"
    }
}