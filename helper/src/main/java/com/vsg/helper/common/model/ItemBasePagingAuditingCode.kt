package com.vsg.helper.common.model

import androidx.room.Ignore
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.helper.helper.Helper.Companion.toPadStart

abstract class ItemBasePagingAuditingCode<T> : ItemBasePagingAuditing<T>(), IEntityCode
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {

    override val code: String
        get() = defaultCode

    @Ignore
    override var number: Int = this.id

    @Ignore
    override var prefix: String = ""

    override var valueCode: String = ""

    override val title: String
        get() = codename
    private val defaultCode: String
        get() = number.toPadStart(LARGE_NUMBER_FORMAT)
    open val codename: String
        get() = when (prefix.isEmpty()) {
            true -> defaultCode
            false -> makeGenericValueCode()
        }

    override val lenCode: Int
        get() = LARGE_NUMBER_FORMAT


    private fun makeGenericValueCode(): String = "${prefix}-${defaultCode}"

    override fun aTitleRecyclerAdapter(): String = codename
    override fun aTitlePopUpData(): String = codename

    override fun equals(other: Any?): Boolean {
        if (other !is ItemBasePagingAuditingCode<*>) return false
        return super.equals(other)
                && code == other.code
                && number == other.number
                && prefix == other.prefix
    }

    override fun hashCode(): Int = layoutPosition

    companion object {
        protected const val LARGE_NUMBER_FORMAT: Int = 10
    }
}