package com.vsg.helper.ui.popup.export

import com.vsg.helper.common.export.ExportType
import com.vsg.helper.ui.popup.parameter.UICustomAlertDialogParameterBase

class UICustomAlertDialogExportParameter :
    UICustomAlertDialogParameterBase<ExportType>("Exportar") {
    init {
        addAll(ExportType.XML, ExportType.JSON, ExportType.CSV)
        factorHeight = calculatedFactorHeight()
    }

    override fun menu(): List<ExportType> = makeMenu { it.value }
}