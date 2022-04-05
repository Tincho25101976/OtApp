package com.vsg.ot.ui.autoFill

import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart

data class RepeatAutoFill(val index: Int, val prefix: String? = null) {
    val position: Int get() = index + 1
    val text: String
        get() = when (prefix.isNullOrEmpty()) {
            true -> position.toPadStart()
            false -> "$prefix ${position.toPadStart()}"
        }
}