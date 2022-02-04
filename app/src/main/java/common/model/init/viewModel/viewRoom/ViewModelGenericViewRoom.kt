package com.vsg.agendaandpublication.common.model.viewModel.viewRoom

import android.app.Application
import com.vsg.agendaandpublication.common.model.itemContact.address.Address
import com.vsg.agendaandpublication.common.model.itemContact.address.AddressViewModel
import com.vsg.agendaandpublication.common.model.itemContact.contact.Contact
import com.vsg.agendaandpublication.common.model.itemContact.contact.ContactViewModel
import com.vsg.agendaandpublication.common.model.itemContact.mail.Mail
import com.vsg.agendaandpublication.common.model.itemContact.mail.MailViewModel
import com.vsg.agendaandpublication.common.model.itemContact.phone.Phone
import com.vsg.agendaandpublication.common.model.itemContact.phone.PhoneViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.accountingSeat.detail.AccountingSeatDetail
import com.vsg.agendaandpublication.common.model.itemOperation.accountingSeat.detail.AccountingSeatDetailViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.accountingSeat.seat.AccountingSeat
import com.vsg.agendaandpublication.common.model.itemOperation.accountingSeat.seat.AccountingSeatViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.batch.Batch
import common.model.master.batch.BatchViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.money.Money
import com.vsg.agendaandpublication.common.model.itemOperation.money.MoneyViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.operation.detail.OperationDetail
import com.vsg.agendaandpublication.common.model.itemOperation.operation.detail.OperationDetailViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.operation.operation.Operation
import com.vsg.agendaandpublication.common.model.itemOperation.operation.operation.OperationViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.transaction.Transaction
import com.vsg.agendaandpublication.common.model.itemOperation.transaction.TransactionViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.Section
import common.model.master.section.SectionViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.Warehouse
import common.model.master.warehouse.WarehouseViewModel
import com.vsg.agendaandpublication.common.model.itemPerson.Person
import com.vsg.agendaandpublication.common.model.itemPerson.PersonViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.category.Category
import com.vsg.agendaandpublication.common.model.itemProduct.category.CategoryViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.picture.Picture
import com.vsg.agendaandpublication.common.model.itemProduct.picture.PictureViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.price.Price
import com.vsg.agendaandpublication.common.model.itemProduct.price.PriceViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import common.model.master.item.ProductViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.unit.UnitViewModel
import common.model.init.viewModel.ViewModelStoredMap
import com.vsg.utilities.common.model.IEntity
import com.vsg.utilities.common.model.viewRoom.IEntityViewRoom
import com.vsg.utilities.common.util.dao.viewRoom.IGenericDaoPagingRelationViewRoom
import com.vsg.utilities.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.utilities.common.util.viewModel.IViewModelView
import com.vsg.utilities.common.util.viewModel.viewRoom.MakeGenericViewModelViewRoom
import kotlin.reflect.KType


@ExperimentalStdlibApi
abstract class ViewModelGenericViewRoom<TDao, TEntity, TViewRoom>(dao: TDao, context: Application) :
    MakeGenericViewModelViewRoom<TDao, TEntity, TViewRoom>(
        dao, context
    )
        where TDao : IGenericDaoPagingRelationViewRoom<TEntity, TViewRoom>,
//              TDao : IDaoAllTextSearchRelation,
              TEntity : IEntityViewRoom<TEntity>,
              TViewRoom : IEntity {


    //region IViewModel
    override fun getInstanceOfIViewModelView(type: KType): IViewModelView<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelView(type, context)
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(type: KType): IViewModelAllSimpleListIdRelation<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelAllSimpleListIdRelation(type, context)
    }
    //endregion

    //region checked
    fun isEntityCheck(entity: IEntity?): Boolean {
        if (entity == null) return false
        return when (entity) {
            is Contact -> ContactViewModel(context).isEntity(entity)
            is Phone -> PhoneViewModel(context).isEntity(entity)
            is Mail -> MailViewModel(context).isEntity(entity)
            is Address -> AddressViewModel(context).isEntity(entity)

            is Product -> ProductViewModel(context).isEntity(entity)
            is Company -> CompanyViewModel(context).isEntity(entity)
            is Category -> CategoryViewModel(context).isEntity(entity)
            is Picture -> PictureViewModel(context).isEntity(entity)
            is Price -> PriceViewModel(context).isEntity(entity)
            is Unit -> UnitViewModel(context).isEntity(entity)

            is Person -> PersonViewModel(context).isEntity(entity)

            is Batch -> BatchViewModel(context).isEntity(entity)

            is AccountingSeat -> AccountingSeatViewModel(context).isEntity(entity)
            is AccountingSeatDetail -> AccountingSeatDetailViewModel(context).isEntity(entity)
            is Money -> MoneyViewModel(context).isEntity(entity)
            is Operation -> OperationViewModel(context).isEntity(entity)
            is OperationDetail -> OperationDetailViewModel(context).isEntity(entity)
            is Transaction -> TransactionViewModel(context).isEntity(entity)
            is Warehouse -> WarehouseViewModel(context).isEntity(entity)
            is Section -> SectionViewModel(context).isEntity(entity)

            else -> false
        }
    }

    fun checkExistsEntityCheck(entity: IEntity?): Boolean {
        if (entity == null) return false
        return when (entity) {
            is Contact -> ContactViewModel(context).checkExistsEntity(entity)
            is Phone -> PhoneViewModel(context).checkExistsEntity(entity)
            is Mail -> MailViewModel(context).checkExistsEntity(entity)
            is Address -> AddressViewModel(context).checkExistsEntity(entity)

            is Product -> ProductViewModel(context).checkExistsEntity(entity)
            is Company -> CompanyViewModel(context).checkExistsEntity(entity)
            is Category -> CategoryViewModel(context).checkExistsEntity(entity)
            is Picture -> PictureViewModel(context).checkExistsEntity(entity)
            is Price -> PriceViewModel(context).checkExistsEntity(entity)
            is Unit -> UnitViewModel(context).checkExistsEntity(entity)

            is Person -> PersonViewModel(context).checkExistsEntity(entity)

            is Batch -> BatchViewModel(context).checkExistsEntity(entity)

            is AccountingSeat -> AccountingSeatViewModel(context).checkExistsEntity(entity)
            is AccountingSeatDetail -> AccountingSeatDetailViewModel(context).checkExistsEntity(
                entity
            )
            is Money -> MoneyViewModel(context).checkExistsEntity(entity)
            is Operation -> OperationViewModel(context).checkExistsEntity(entity)
            is OperationDetail -> OperationDetailViewModel(context).checkExistsEntity(entity)
            is Transaction -> TransactionViewModel(context).checkExistsEntity(entity)
            is Warehouse -> WarehouseViewModel(context).checkExistsEntity(entity)
            is Section -> SectionViewModel(context).checkExistsEntity(entity)

            else -> false
        }
    }
    //endregion

}