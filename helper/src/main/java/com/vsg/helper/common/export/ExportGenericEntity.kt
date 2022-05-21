package com.vsg.helper.common.export

class ExportGenericEntity(override val type: Class<*>) : IExportSimpleFormat, IExport {
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
    override fun nameFile(exportType: ExportType): String =
        "${type.simpleName}Source.${exportType.extension}"
//endregion
}