package common.helper

import common.enumType.TypeUnit
import common.model.master.MasterUnit

class HelperMaster {
    companion object {
        fun TypeUnit.toMasterUnit(precision: Int = 3): MasterUnit =
            MasterUnit(this).apply { this@toMasterUnit.precision = precision }
    }
}