package com.vsg.helper.util.quantity.interfaces

interface IQtyRange {
    var from: Double
    var to: Double
    val diff: Double
    var format: String

    fun setRange(from: Double, to: Double)
    fun setRange(range: IQtyRange)
}