package com.vsg.ot.ui.common.securityDigital.xact.rating

import com.vsg.helper.ui.data.ui.DataBaseActivity
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRating
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRatingDao
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRatingViewModel

@ExperimentalStdlibApi
class UIUpdateDataXactRating : DataBaseActivity<XactRatingViewModel, XactRatingDao, XactRating>(
    XactRatingViewModel::class.java
) {
    override fun aGetEntity(): XactRating = XactRating()
}