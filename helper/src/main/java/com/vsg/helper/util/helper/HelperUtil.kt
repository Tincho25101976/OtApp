package com.vsg.helper.util.helper

import com.vsg.helper.util.unit.Unit
import com.vsg.helper.util.unit.type.TypeUnit

class HelperUtil {
    companion object {
        fun TypeUnit.toUnit(precision: Int = 3): Unit =
            Unit(this).apply { this@toUnit.precision = precision }
    }
}