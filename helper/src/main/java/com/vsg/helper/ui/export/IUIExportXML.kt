package com.at.ui.export

import com.vsg.helper.common.export.IExport

interface IUIExportXML {
    fun export(data: IExport): String
}