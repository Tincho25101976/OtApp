package com.vsg.agendaandpublication.common.model.itemOperation.batch

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.agendaandpublication.common.model.itemOperation.batch.type.TypeBatchStatus
import com.vsg.agendaandpublication.common.model.itemOperation.money.Money
import com.vsg.agendaandpublication.common.model.itemOperation.money.MoneyViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.operation.operation.Operation
import com.vsg.agendaandpublication.common.model.itemOperation.operation.operation.OperationViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.operation.util.FilterClassOperation
import com.vsg.agendaandpublication.common.model.itemOperation.operation.util.OperationRelation
import com.vsg.agendaandpublication.common.model.itemOperation.operation.util.OperationRelations
import com.vsg.agendaandpublication.common.model.itemOperation.stock.StockDTO
import com.vsg.agendaandpublication.common.model.itemOperation.stock.StockViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.Section
import com.vsg.agendaandpublication.common.model.itemPerson.Person
import com.vsg.agendaandpublication.common.model.itemPerson.PersonViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import common.model.master.company.CompanyViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import common.model.master.item.ProductViewModel
import com.vsg.utilities.common.model.IEntity
import com.vsg.utilities.common.util.viewModel.IViewModelCRUD
import com.vsg.utilities.common.util.viewModel.util.FilterMemberInclude
import com.vsg.utilities.helper.Helper.Companion.toCount
import com.vsg.utilities.helper.date.HelperDate.Companion.now
import common.model.master.batch.BatchDao
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class BatchViewModel(context: Application) :
    ViewModelGenericForCode<BatchDao, Batch>(
        AppDatabase.getInstance(context)?.batchDao()!!, context
    ) {

    //region add
    override fun viewModelInsert(item: Batch): Boolean {
        val code = viewModelEncode(item) ?: return false
        return super.viewModelInsert(code)
//        return false
    }

    override fun viewModelEncode(item: Batch): Batch? {
        item.number = viewModelNextAutoCode(item.idCompany)
        item.prefix = when (item.product == null) {
            true -> ProductViewModel(context).viewModelGetPrefix(item.idProduct)
            false -> item.product?.prefixBatch ?: ""
        }
        item.valueCode = item.makeGenericValueCode()
        if (item.valueCode.isEmpty()) return null
        return item
    }

    fun addBatchOperationCorrectionInput(
        company: Company,
        money: Money,
        batch: List<StockDTO>,
        user: Person,
        isAddLote: Boolean = false
    ): Boolean {
        return addBatchOperation(
            company,
            money,
            batch,
            user,
            FilterClassOperation.CORRECTION_INPUT,
            isAddLote
        )
    }

    fun addBatchOperationCorrectionOutput(
        company: Company,
        money: Money,
        batch: List<StockDTO>,
        user: Person
    ): Boolean {
        return addBatchOperation(
            company,
            money,
            batch,
            user,
            FilterClassOperation.CORRECTION_OUTPUT
        )
    }

    fun addBatchOperationPurchaseInput(
        company: Company,
        money: Money,
        batch: List<StockDTO>,
        provider: Person,
        isAddLote: Boolean = true
    ): Boolean {
        return addBatchOperation(
            company,
            money,
            batch,
            provider,
            FilterClassOperation.PURCHASE_INPUT,
            isAddLote
        );
    }

    fun addBatchOperationPurchaseOutput(
        company: Company,
        operation: Operation,
        batch: List<StockDTO>
    ): Boolean {
        return addDetail(company, operation, batch, FilterClassOperation.PURCHASE_OUTPUT)
    }

    fun addBatchOperationSaleInput(
        company: Company,
        money: Money,
        batch: List<StockDTO>,
        client: Person
    ): Boolean {
        return addBatchOperation(company, money, batch, client, FilterClassOperation.SALE_INPUT)
    }

    fun addBatchOperationSaleOutput(
        company: Company,
        operation: Operation,
        batch: List<StockDTO>
    ): Boolean {
        return addDetail(company, operation, batch, FilterClassOperation.SALE_OUTPUT)
    }

    fun addBatchOperationCorrectionTransference(
        company: Company,
        money: Money,
        batch: StockDTO,
        user: Person,
        section: Section
    ): Boolean {
        return addBatchOperationTransference(company, money, batch, user, section)
    }
    //endregion

    //region embebed
    fun viewModelGetBatchProductByProduct(idRelation: Long): List<Batch>? {
        val list = dao.viewAllSimpleListBatchProductByProduct(idRelation) ?: return null
        val temp: MutableList<Batch?> = mutableListOf()
        list.mapTo(temp) {
            viewModelBatchProduct(it.id)
        }
        return temp.filterNotNull()
    }

    fun viewModelGetBatchProductByBatch(idRelation: Long): Batch? {
        val temp = dao.viewAllSimpleBatchProductByBatch(idRelation) ?: return null
        return viewModelBatchProduct(temp.id)
    }

    private fun viewModelBatchProduct(id: Long): Batch? {
        return this.viewModelView(
            id,
            include = arrayOf(FilterMemberInclude(entity = typeOf<Product>()).apply {
                addEntity(
                    typeOf<Unit>()
                )
            })
        )
    }
    //endregion

    fun viewModelGetProduct(idRelation: Long): Product? =
        ProductViewModel(context).viewModelView(idRelation)

    //region transaction

    //region alertaVencimiento
    fun viewModelGetDueDateAlert(empresa: Company): List<StockDTO>? {
        var e: List<StockDTO>?
        try {
            e = StockViewModel(context).getStock(empresa)
            if (e == null || !e.any()) {
                onRaiseException("No hay stock disponible para esta empresa", 2)
                return null
            }
            e = e.filter { s -> s.expirationStatus == TypeBatchStatus.NEAR_EXPIRY }.toList()
        } catch (ex: Exception) {
            e = null
            onRaiseException(ex, 3)
        }
        return e
    }
    //endregion

    //region privates
    private fun addBatchOperation(
        company: Company,
        money: Money,
        stock: List<StockDTO>,
        person: Person,
        filterClassOperation: FilterClassOperation,
        addLote: Boolean = false
    ): Boolean {
        CompanyViewModel(context).checkExistsEntity(company)
        MoneyViewModel(context).checkExistsEntity(money)
        PersonViewModel(context).checkExistsEntity(person)
        val relation: OperationRelation? =
            OperationRelations().getOperationRelation(filterClassOperation)
        if (relation == null) onRaiseExceptionOperationRelation(filterClassOperation)
        var x = false
        try {
            dataForTransaction().runInTransaction {
                if (addLote) stock.forEach { viewModelInsert(it.batch) }
                val operation = OperationViewModel(context).setOperation(
                    company,
                    money,
                    relation!!,
                    person,
                    stock,
                    section = null
                )
                x = addItems(operation)
                if (!x) throw Exception("ROLLBACK")
            }
        } catch (ex: Exception) {
            x = false
            onRaiseException(ex, 4)
        }
        return x
    }

    private fun addBatchOperationTransference(
        empresa: Company,
        moneda: Money,
        stock: StockDTO,
        persona: Person,
        section: Section
    ): Boolean {
        checkExistsEntityCheck(empresa)
        checkExistsEntityCheck(moneda)
        checkExistsEntityCheck(persona)
        checkExistsEntityCheck(section)
        val relation: OperationRelation? =
            OperationRelations().getOperationRelation(FilterClassOperation.TRANSFER_INPUT)
        if (relation == null) onRaiseExceptionOperationRelation(FilterClassOperation.TRANSFER_INPUT)
        var x = false
        try {
            AppDatabase.getInstance(context)!!.runInTransaction {
                val operation = OperationViewModel(context).setOperation(
                    empresa,
                    moneda,
                    relation!!,
                    persona,
                    listOf(stock),
                    now(),
                    section
                )
                x = addItems(operation)
                if (!x) throw Exception("ROLLBACK")
            }
        } catch (ex: Exception) {
            x = false
            onRaiseException(ex, 5)
        }
        return x
    }


    private fun addDetail(
        company: Company,
        operation: Operation,
        stock: List<StockDTO>,
        filterClassOperation: FilterClassOperation
    ): Boolean {
        if (!stock.any()) {
            onRaiseException(
                "No se ha suministrado una lista de stock para agregar a la operación: ${operation.valueCode}",
                1
            )
            return false
        }
        var e = 0
        try {
            val relation = OperationRelation(filterClassOperation)
            val vOperation = OperationViewModel(context)
            stock.forEach { s ->
                e += when (vOperation.addDetail(company, operation, s, relation)) {
                    true -> 1
                    false -> 0
                }
            }
        } catch (ex: Exception) {
            onRaiseException(ex, 6)
        }
        return e == stock.count()
    }

    //region add Operation
    private fun addItems(operation: Operation?): Boolean {
        if (operation == null) return false
//        var x = OperationViewModel(context).viewModelInsert(operation)
//        if (x)
//        var x = addItem(operation.operationDetail, OperationDetailViewModel(context))
//        if (x) {
//            val temp: List<AccountingSeat> = operation.operationDetail.flatMap { it.accountingSeat }
//            x = addItem(temp, AccountingSeatViewModel(context))
//        }
//        if (x) {
//            val temp: List<AccountingSeatDetail> =
//                operation.operationDetail.flatMap { it.accountingSeat.flatMap { it.accountingSeatDetail } }
//            x = addItem(temp, AccountingSeatDetailViewModel(context))
//        }
//        if (x) {
//            val temp: List<Transaction> = operation.operationDetail.flatMap { it.transaction }
//            x = addItem(temp, TransactionViewModel(context))
//        }
        return true
    }

    private fun <T : IEntity> addItem(list: List<T>, viewModel: IViewModelCRUD<T>): Boolean {
        var count = 0
        var temp = 0
        list.forEach { tr ->
            temp++
            count += viewModel.viewModelInsert(tr).toCount()
        }
        return count == temp
    }
    //endregion

    //endregion

    //endregion
}