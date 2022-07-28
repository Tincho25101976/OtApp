package com.vsg.ot.common.model.securityDialog.security.group

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
import com.vsg.ot.common.model.securityDialog.security.process.SecurityProcess
import com.vsg.ot.common.model.securityDialog.security.item.SecurityItem

@Entity(
    indices = [
        Index(
            value = arrayOf("valueCode", "description", "idProcess"),
            name = "IX_SECURITY_GROUP"
        )],
    inheritSuperIndices = true,
    tableName = SecurityGroup.ENTITY_NAME
)
class SecurityGroup : EntityOtParseWithExport<SecurityGroup>() {

    //region properties
    @Ignore
    var items: List<SecurityItem>? = null

    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idProcess: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    var securityProcess: SecurityProcess? = null
    //endregion

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_security_dialog_group

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SecurityGroup) return false
        return valueCode == other.valueCode
                && description == other.description
                && idProcess == other.idProcess
    }

    override fun getFields(): List<String> {
        return listOf("id", "valueCode", "description", "isDefault", "isEnabled", "idProcess")
    }

    override fun aGetExportItem(): List<ExportGenericEntityItem<*>> {
        return listOf<ExportGenericEntityItem<out Any>>(
            ExportGenericEntityItem("id", value = id),
            ExportGenericEntityItem("valueCode", value = valueCode),
            ExportGenericEntityItem("description", value = description),
            ExportGenericEntityItem("isDefault", value = isDefault),
            ExportGenericEntityItem("isEnabled", value = isEnabled),
            ExportGenericEntityItem("idProcess", value = idProcess)
        )
    }

    override fun aGetItemCast(): SecurityGroup = SecurityGroup()
    //endregion

    companion object {
        const val ENTITY_NAME = "masterSecurityGroup"
    }
}