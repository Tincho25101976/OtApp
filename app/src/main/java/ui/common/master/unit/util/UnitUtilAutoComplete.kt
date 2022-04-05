package com.vsg.agendaandpublication.ui.common.itemProduct.unit.util

import com.vsg.agendaandpublication.common.model.itemProduct.unit.Unit

class UnitUtilAutoComplete {
    val list: List<Unit> = listOf(
        getUnit("Gramos", "gr", true, 3),
        getUnit("Kilogramos", "kg", true, 3),
        getUnit("Mililitros", "ml", true, 3),
        getUnit("Litros", "lt", true, 3),
        getUnit("Volumen", "cm³", true, 3),
        getUnit("Unidad", "un", false, 0),
        getUnit("Pack", "pack", false, 0),
        getUnit("Caja", "caja", false, 0),
        getUnit("Sin dimensión", "[]", false, 0),
    )

    private fun getUnit(name: String, symbol: String, isDecimal: Boolean, decimals: Int) =
        Unit().apply {
            this.name = name
            this.symbol = symbol
            this.isDecimal = isDecimal
            this.decimals = decimals
            this.isDefault = false
            this.isEnabled = true
            this.description = ""
        }
}