package com.vsg.helper.common.util.dao


interface IDaoNextCode {
    fun viewNextAutoCode(idRelation: Int): Int
}