package common.helper

import common.model.common.unit.type.TypeUnit
import common.model.common.unit.Unit

class HelperMaster {
    companion object {
        fun TypeUnit.toUnit(precision: Int = 3): Unit =
            Unit(this).apply { this@toUnit.precision = precision }
    }
}