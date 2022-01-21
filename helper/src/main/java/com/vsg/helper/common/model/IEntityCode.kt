package com.vsg.helper.common.model

interface IEntityCode {
    var code: String
    var prefix: String
    var number: Long
    var valueCode: String

    val lenCode: Int
}