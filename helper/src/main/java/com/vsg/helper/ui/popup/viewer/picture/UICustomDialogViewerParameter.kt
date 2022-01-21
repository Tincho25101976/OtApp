package com.vsg.helper.ui.popup.viewer.picture

import android.graphics.Bitmap
import com.vsg.helper.common.popup.IPopUpParameter

class UICustomDialogViewerParameter(var bitmap: Bitmap): IPopUpParameter {
    override var factorHeight: Double = 0.75
        get() {
            if (field <= 0.0) return 0.1
            if (field >= 1.0) return 1.0
            return field
        }
    override var factorWidth: Double = 0.90
        get() {
            if (field <= 0.0) return 0.1
            if (field >= 1.0) return 1.0
            return field
        }
    override var canceledOnTouchOutside: Boolean = false
    internal fun isBitmap(): Boolean = bitmap.byteCount > 0

}