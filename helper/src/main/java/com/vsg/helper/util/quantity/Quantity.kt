package com.vsg.helper.util.quantity

import com.vsg.helper.util.quantity.interfaces.IQty

class Quantity: IQty {
    //region properties
    var override quantity: Double = 0.0

    var verificarLimites: Boolean = false
    var minimo: Double = 0.0
        private set(value) { field = value }
    var maximo: Double = 0.0
        private set(value) { field = value }
    //endregion
}