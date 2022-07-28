package com.vsg.ot.common.model.securityDialog.security.item

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.common.export.ExportGenericEntityItem
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.init.entity.EntityOtParseWithExport
import com.vsg.ot.common.model.securityDialog.security.group.SecurityGroup

@Entity(
    indices = [
        Index(
            value = arrayOf("valueCode", "description", "value", "idGroup"),
            name = "IX_SECURITY_ITEM"
        )],
    inheritSuperIndices = true,
    tableName = SecurityItem.ENTITY_NAME
)
class SecurityItem : EntityOtParseWithExport<SecurityItem>() {

    //region properties
    var value: Boolean = false

    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idGroup: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    var securityGroup: SecurityGroup? = null
    //endregion

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_security_dialog_item

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SecurityItem) return false
        return valueCode == other.valueCode
                && description == other.description
                && idGroup == other.idGroup
                && value == other.value
    }

    override fun getFields(): List<String> {
        return listOf(
            "id",
            "valueCode",
            "description",
            "isDefault",
            "isEnabled",
            "idGroup",
            "value"
        )
    }

    override fun aGetExportItem(): List<ExportGenericEntityItem<*>> {
        return listOf<ExportGenericEntityItem<out Any>>(
            ExportGenericEntityItem("id", value = id),
            ExportGenericEntityItem("valueCode", value = valueCode),
            ExportGenericEntityItem("description", value = description),
            ExportGenericEntityItem("isDefault", value = isDefault),
            ExportGenericEntityItem("isEnabled", value = isEnabled),
            ExportGenericEntityItem("idGroup", value = idGroup),
            ExportGenericEntityItem("value", value = value)
        )
    }

    override fun aGetItemCast(): SecurityItem = SecurityItem()
    //endregion

    companion object {
        const val ENTITY_NAME = "masterSecurityItem"
    }
}