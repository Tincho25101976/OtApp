package com.vsg.ot.ui.common.securityDigital.xact.sector

import com.vsg.helper.helper.progress.IActivityForProgress
import com.vsg.helper.ui.data.ui.DataBaseActivity
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorDao
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorViewModel

@ExperimentalStdlibApi
class UIUpdateDataXactSector<TActivity>(activity: TActivity) :
    DataBaseActivity<TActivity, XactSectorViewModel, XactSectorDao, XactSector>(
        activity
    )
        where TActivity : CurrentBaseActivity<XactSectorViewModel, XactSectorDao, XactSector>,
              TActivity : IActivityForProgress {
    override fun aGetItem(): XactSector = XactSector()
}