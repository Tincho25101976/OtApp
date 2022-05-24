package com.vsg.helper.common.report

import com.vsg.helper.common.export.ExportGenericEntity
import com.vsg.helper.common.export.IExport

interface IEntityReport: IExport {
    fun report(): ExportGenericEntity
}