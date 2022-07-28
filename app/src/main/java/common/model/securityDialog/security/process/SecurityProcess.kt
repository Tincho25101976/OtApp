package com.vsg.ot.common.model.securityDialog.security.process

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.common.export.ExportGenericEntityItem
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.init.entity.EntityOtWithPictureParseWithExportReport
import com.vsg.ot.common.model.securityDialog.security.group.SecurityGroup
import com.vsg.ot.common.model.securityDialog.security.reference.SecurityReference
import com.vsg.ot.common.model.securityDialog.security.type.TypeShift
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import common.model.master.item.type.TypePlant
import java.util.*

@Entity(
    indices = [
        Index(
            value = arrayOf("valueCode", "description"),
            name = "IX_SECURITY_PROCESS"
        ),
        Index(
            value = arrayOf("idSector", "idReference", "createDate"),
            name = "IX_SECURITY_PROCESS_FK"
        )],
    inheritSuperIndices = true,
    tableName = SecurityProcess.ENTITY_NAME
)
class SecurityProcess : EntityOtWithPictureParseWithExportReport<SecurityProcess>() {

    //region properties
    var shift: TypeShift = TypeShift.UNDEFINED
    var time: Int = 0
    var contactada: Int = 0
    var observada: Int = 0

    var actoSeguro: String = ""
    var actoInseguro: String = ""
    var medidaCorrectiva: String = ""
    var planta: TypePlant = TypePlant.UNDEFINED

    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idSector: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    var sector: XactSector? = null

    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idReference: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var reference: SecurityReference? = null

    @Ignore
    var securityGroup: List<SecurityGroup>? = null
    //endregion

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_security_dialog

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SecurityProcess) return false
        return valueCode == other.valueCode
                && description == other.description
                && idSector == other.idSector
                && idReference == other.idReference
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

    override fun aGetItemCast(): SecurityProcess = SecurityProcess()

    override fun aGetReportItem(): List<ExportGenericEntityItem<*>> {
        return listOf<ExportGenericEntityItem<out Any>>(
            ExportGenericEntityItem("picture", value = picture),
            ExportGenericEntityItem("shift", value = shift.title, nameReport = "Turno"),
            ExportGenericEntityItem("time", value = time, nameReport = "Tiempo"),
            ExportGenericEntityItem("contactada", value = contactada, nameReport = "P Contactada"),
            ExportGenericEntityItem("observada", value = observada, nameReport = "P Observada"),
            ExportGenericEntityItem("actoSeguro", value = actoSeguro, nameReport = "Acto Seguro"),
            ExportGenericEntityItem(
                "actoInseguro",
                value = actoInseguro,
                nameReport = "Acto Inseguro"
            ),
            ExportGenericEntityItem(
                "medidaCorrectiva",
                value = medidaCorrectiva,
                nameReport = "Medida Correctiva"
            ),
            ExportGenericEntityItem("planta", value = planta.title, nameReport = "Planta"),
            ExportGenericEntityItem(
                "idReference",
                value = reference?.description,
                nameReport = "Referencia"
            ),
            ExportGenericEntityItem("idSector", value = sector?.description, nameReport = "Sector"),
            ExportGenericEntityItem("createDate", value = createDate, nameReport = "Fecha"),
            ExportGenericEntityItem("description", value = description, nameReport = "Observación")
        )
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterSecurityProcess"
    }
}