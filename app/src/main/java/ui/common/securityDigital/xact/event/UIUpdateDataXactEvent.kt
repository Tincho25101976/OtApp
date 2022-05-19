package com.vsg.ot.ui.common.securityDigital.xact.event

import com.vsg.helper.ui.data.ui.DataBaseActivity
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventDao
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventViewModel

@ExperimentalStdlibApi
class UIUpdateDataXactEvent : DataBaseActivity<XactEventViewModel, XactEventDao, XactEvent>(
    XactEventViewModel::class.java
) {
    override fun aGetEntity(): XactEvent = XactEvent()
}