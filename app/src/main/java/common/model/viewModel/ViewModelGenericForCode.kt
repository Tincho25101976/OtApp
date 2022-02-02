package common.model.viewModel

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.agendaandpublication.common.model.itemOperation.batch.BatchViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseViewModel
import common.model.master.item.ProductViewModel
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.common.util.dao.IDaoAllTextSearchRelation
import com.vsg.helper.common.util.dao.IDaoNextCode
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPagingRelationCode
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.MakeGenericViewModelPagingRelationCode
import common.model.master.batch.MasterBatch
import common.model.master.company.CompanyViewModel
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.section.MasterSection
import common.model.master.warehouse.MasterWarehouse
import kotlin.reflect.KType

@ExperimentalStdlibApi
abstract class ViewModelGenericForCode<TDao, TEntity>(dao: TDao, context: Application) :
    MakeGenericViewModelPagingRelationCode<TDao, TEntity>(
        dao, context
    )
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPagingRelationCode<TEntity>,
              TDao : IDaoNextCode,
              TDao : IDaoAllTextSearchRelation,
              TEntity : IEntity,
              TEntity : IEntityCode,
              TEntity : IIsEnabled,
              TEntity : IIsDefault,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {


    protected fun dataForTransaction(): AppDatabase = AppDatabase.getInstance(context)!!

    //region checked
    fun isEntityCheck(entity: IEntity?): Boolean {
        if (entity == null) return false
        return when (entity) {
            is MasterItem -> ProductViewModel(context).isEntity(entity)
            is MasterCompany -> CompanyViewModel(context).isEntity(entity)
            is MasterBatch -> BatchViewModel(context).isEntity(entity)
            is MasterWarehouse -> WarehouseViewModel(context).isEntity(entity)
            is MasterSection -> SectionViewModel(context).isEntity(entity)

            else -> false
        }
    }

    fun checkExistsEntityCheck(entity: IEntity?): Boolean {
        if (entity == null) return false
        return when (entity) {
            is MasterItem -> ProductViewModel(context).checkExistsEntity(entity)
            is MasterCompany -> CompanyViewModel(context).checkExistsEntity(entity)
            is MasterBatch -> BatchViewModel(context).checkExistsEntity(entity)
            is MasterWarehouse -> WarehouseViewModel(context).checkExistsEntity(entity)
            is MasterSection -> SectionViewModel(context).checkExistsEntity(entity)

            else -> false
        }
    }
    //endregion

    //region IViewModel
    override fun getInstanceOfIViewModelView(type: KType): IViewModelView<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelView(type, context)
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(type: KType): IViewModelAllSimpleListIdRelation<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelAllSimpleListIdRelation(type, context)
    }
    //endregion
}