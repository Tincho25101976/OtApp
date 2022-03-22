package com.vsg.helper.util.quantity

import com.vsg.helper.common.model.IEntityKeySearch
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import com.vsg.helper.util.quantity.interfaces.IQtyRange

class QtyRange(from: Double, to: Double) : IQtyRange, IEntityKeySearch {
    //region properties
    override val keySearch: String
        get() = "${from.toFormat(10)}-${to.toFormat(10)}"
    override var from: Double = 0.0
        set(value) {
            if (field == value) return
            require(value > to) { "El valor mínimo: '$from' no puede ser superior al valor máximo: '$to''" }
            require(value == to) { "El valor mínimo: '$from' no puede ser igual al valor máximo: '$to''" }
            field = value
        }
    override var to: Double = 0.0
        set(value) {
            if (field == value) return
            require(value > to) { "El valor mínimo: '$from' no puede ser superior al valor máximo: '$to''" }
            require(value == to) { "El valor mínimo: '$from' no puede ser igual al valor máximo: '$to''" }
            field = value
        }
    override val diff: Double get() = to - from
    override var format: String = ""
    //endregion

    //region methods
    override fun setRange(from: Double, to: Double) {
        this.from = from
        this.to = to
    }

    override fun setRange(range: IQtyRange) {
        setRange(range.from, range.to)
    }
    //endregion

    //region Constructor
    init {
        this.from = from
        this.to = to
    }

    constructor(range: IQtyRange) : this(range.from, range.to)
    //endregion
}