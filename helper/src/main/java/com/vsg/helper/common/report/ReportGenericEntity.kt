package com.vsg.helper.common.report

import com.vsg.helper.common.export.*
import com.vsg.helper.helper.file.HelperFile.Static.getNameFile

class ReportGenericEntity(override val type: Class<*>) : IExportSimpleFormat, IExport {
    //region properties
    val items: MutableList<ExportGenericEntityItem<*>> = mutableListOf()
    val hasItems: Boolean get() = items.any()
    val values: List<ExportGenericEntityValue>
        get() {
            val result = items.map {
                ExportGenericEntityValue(type.simpleName, it.name, it.value)
            }
            return result
        }
//endregion

    //region methods
    override fun nameFile(exportType: ExportType, addTime: Boolean): String =
        type.getNameFile(exportType.extension, addTime)
//endregion
}