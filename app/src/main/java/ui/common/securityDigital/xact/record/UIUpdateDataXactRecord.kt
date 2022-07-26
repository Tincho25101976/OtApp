package com.vsg.ot.ui.common.securityDigital.xact.record

import android.view.Menu
import com.vsg.helper.ui.data.ui.DataBaseActivity
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordDao
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordViewModel

@ExperimentalStdlibApi
class UIUpdateDataXactRecord : DataBaseActivity<XactRecordViewModel, XactRecordDao, XactRecord>(
    XactRecordViewModel::class.java
) {
    override fun aGetEntity(): XactRecord = XactRecord()
}