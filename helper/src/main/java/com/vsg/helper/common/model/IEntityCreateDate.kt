package com.vsg.helper.common.model

import com.vsg.helper.common.format.FormatDateString
import java.util.*

interface IEntityCreateDate {
    var createDate: Date
    val formatDate: FormatDateString
}