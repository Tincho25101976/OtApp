package com.vsg.helper.ui.widget.text.type

import com.vsg.helper.common.utilEnum.IValue

enum class TypeCustomInputText(override val value: Int, val type: TypeCustomInputTextType) : IValue {
    TEXT(1, TypeCustomInputTextType.TEXT),
    TEXT_MULTILINE(2, TypeCustomInputTextType.TEXT),
    DATE(3, TypeCustomInputTextType.DATE),
    DOUBLE(4, TypeCustomInputTextType.NUMERIC),
    INT(5, TypeCustomInputTextType.NUMERIC),
    PHONE(6,TypeCustomInputTextType.TEXT),
    READ_ONLY_TEXT(7, TypeCustomInputTextType.TEXT),
    READ_ONLY_MULTILINE(8, TypeCustomInputTextType.TEXT)
}