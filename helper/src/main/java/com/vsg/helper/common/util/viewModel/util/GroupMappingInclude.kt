package com.vsg.helper.common.util.viewModel.util

import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

open class GroupMappingInclude {
    var groupId: Int = 0
    var name: String = ""
    var idEntity: Int = 0

    var type: KType? = null
    val javaType: Type?
        get() = when (type == null) {
            false -> type?.javaType
            true -> null
        }
    val isProcess: Boolean
        get() = groupId != 0 && name.isNotEmpty() && idEntity > 0 && type != null
}