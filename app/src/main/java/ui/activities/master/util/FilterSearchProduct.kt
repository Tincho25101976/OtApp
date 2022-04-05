package com.vsg.agendaandpublication.ui.activities.itemProducto.util

import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import com.vsg.utilities.common.model.IEntity
import com.vsg.utilities.ui.adapter.IDataAdapterTitle
import kotlinx.coroutines.runBlocking

class FilterSearchProduct(val type: FilterTypeActivityProduct, override var id: Long) :
    IEntity, IDataAdapterTitle {
    override val title: String
        get() = type.title

//    suspend fun query(find: String): (Product) -> Boolean = runBlocking {
//        return@runBlocking type.query(find)
//    }
}