package com.vsg.ot.ui.common.securityDigital.xact.sector

import com.vsg.helper.ui.data.ui.DataBaseActivity
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorDao
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorViewModel

@ExperimentalStdlibApi
class UIUpdateDataXactSector : DataBaseActivity<XactSectorViewModel, XactSectorDao, XactSector>(
    XactSectorViewModel::class.java
) {
    override fun aGetEntity(): XactSector = XactSector()
}