package com.vsg.helper.common.model

interface IEntityParse<T> : IEntity where T : IEntity {
    val tag: String
    fun getFields(): List<String>
    fun cast(s: HashMap<String?, String?>): T
}