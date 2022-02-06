package common.helper

import common.model.master.unit.type.TypeUnit
import common.model.master.unit.MasterUnit

class HelperMaster {
    companion object {
        fun TypeUnit.toMasterUnit(precision: Int = 3): MasterUnit =
            MasterUnit(this).apply { this@toMasterUnit.precision = precision }
    }
}