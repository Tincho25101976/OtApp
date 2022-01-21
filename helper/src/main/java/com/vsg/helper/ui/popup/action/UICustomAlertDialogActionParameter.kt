package com.vsg.helper.ui.popup.action

import com.vsg.helper.ui.popup.parameter.UICustomAlertDialogParameterBase

class UICustomAlertDialogActionParameter(sizeImage: Int = 42) :
    UICustomAlertDialogParameterBase<UICustomAlertDialogActionType>("Acciones") {
    init {
        addAll(
            UICustomAlertDialogActionType.DELETE,
            UICustomAlertDialogActionType.UPDATE,
            UICustomAlertDialogActionType.EXPORT,
            UICustomAlertDialogActionType.VIEW
        )
        factorHeight = calculatedFactorHeight()
        this.sizeImage = sizeImage
    }

    override fun menu(): List<UICustomAlertDialogActionType> = makeMenu { it.value }
}