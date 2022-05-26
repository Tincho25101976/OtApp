package com.vsg.helper.common.export

interface IExport {
    fun nameFile(exportType: ExportType, addTime: Boolean): String
}
