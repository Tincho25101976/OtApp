package com.vsg.helper.common.model

interface IEntityCode {
    val code: String
    var prefix: String
    var number: Int
    var valueCode: String

    val lenCode: Int
}