package com.vsg.helper.ui.popup.dialog

import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import com.vsg.helper.common.popup.IPopUpParameter

abstract class UICustomAlertDialogParameter(
    @LayoutRes val layout: Int,
    override var canceledOnTouchOutside: Boolean = false
) : IPopUpParameter {
    @FloatRange(from = 0.1, to = 1.0, fromInclusive = true, toInclusive = true)
    override var factorHeight: Double = FACTOR
        get() {
            if (field <= 0) return 0.1
            if (field >= 1.0) return 1.0
            return field
        }

    @FloatRange(from = 0.1, to = 1.0, fromInclusive = true, toInclusive = true)
    override var factorWidth: Double = FACTOR
        get() {
            if (field <= 0) return 0.1
            if (field >= 1.0) return 1.0
            return field
        }
    open var title: String = ""

    fun isProcess(): Boolean {
        if (layout <= 0) return false
        return true
    }

    companion object Static {
        const val FACTOR: Double = 0.8
    }
}