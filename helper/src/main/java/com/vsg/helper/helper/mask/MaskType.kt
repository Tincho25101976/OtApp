package com.vsg.helper.helper.mask

import android.text.InputType

enum class MaskType(val mask: String, val digits: String, val type: Int) {

    PHONE("{(}[00]{)} [0000]-[0000]", "0123456789", InputType.TYPE_CLASS_PHONE),
    DATE(
        "[00]{/}[00]{/}[0000]",
        "0123456789/",
        InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE or InputType.TYPE_DATETIME_VARIATION_TIME
    ),
    NUMBER_DECIMAL(
        "[9999999]{.}[9999]",
        "0123456789.-",
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    ),
    NUMBER_INTEGER("[0000]", "0123456789-", InputType.TYPE_CLASS_NUMBER),
    MAIL_ADDRESS("[A]{@}[A]{.com}", "0123456789", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)

}
