package com.vsg.helper.ui.widget.text.type

import com.vsg.helper.common.utilEnum.IValue

enum class TypeCustomInputText(override val value: Int) : IValue {
    TEXT(1),
    TEXT_MULTILINE(2),
    DATE(3),
    DOUBLE(4),
    INT(5),
    PHONE(6),
    READ_ONLY_TEXT(7),
    READ_ONLY_MULTILINE(8)
}