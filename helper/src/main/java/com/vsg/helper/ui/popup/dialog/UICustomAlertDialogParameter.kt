package com.vsg.helper.ui.popup.dialog

import androidx.annotation.LayoutRes
import com.vsg.helper.common.popup.IPopUpParameter

abstract class UICustomAlertDialogParameter(
    @LayoutRes val layout: Int,
    override var canceledOnTouchOutside: Boolean = false
): IPopUpParameter {
    override var factorHeight: Double = 0.8
        get() {
            if (field <= 0) return 0.1
            if (field >= 1.0) return 1.0
            return field
        }
    override var factorWidth: Double = 0.8
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