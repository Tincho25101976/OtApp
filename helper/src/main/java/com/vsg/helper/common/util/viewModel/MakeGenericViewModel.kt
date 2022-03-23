package com.vsg.helper.common.util.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.common.model.EntityForeignKeyList
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.model.viewModel.IViewModelStoredMap
import com.vsg.helper.common.util.dao.IDaoCheckExitsEntity
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.util.FilterMemberInclude
import com.vsg.helper.common.util.viewModel.util.GroupMappingInclude
import com.vsg.helper.common.util.viewModel.util.MappingInclude
import com.vsg.helper.common.util.viewModel.util.MappingMembers
import com.vsg.helper.helper.Helper.Companion.toCount
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

abstract class MakeGenericViewModel<TDao, TEntity>(
    protected val dao: TDao,
    val context: Application,
    val stored: IViewModelStoredMap
) : AndroidViewModel(context),
    IViewModelUpdateSetEnabled<TEntity>,
    IViewModelView<TEntity>,
    IViewModelCheckExistsEntity<TEntity>,
    IViewModelCRUD<TEntity>
        where TDao : IGenericDao<TEntity>,
              TDao : IDaoCheckExitsEntity,
              TEntity : IEntity,
              TEntity : IIsEnabled {

    //region handler
    var onEventProcess: ((String) -> Unit)? = null
    //endregion

    //region crud
    override fun viewModelInsert(item: TEntity): Boolean = runBlocking {
        val tempId: Int = dao.insert(item)
        item.id = tempId
        return@runBlocking tempId > 0
    }

    override fun viewModelInsert(item: List<TEntity>): Boolean =
        runBlocking {
            if (!item.any()) return@runBlocking false
            var i = 0
            item.forEach { i += (dao.insert(it) > 0).toCount() }
            return@runBlocking i == item.count()
        }

    override fun viewModelUpdate(item: TEntity): Boolean = runBlocking {
        return@runBlocking dao.update(item) > 0
    }

    override fun viewModelDelete(item: TEntity): Boolean = runBlocking {
        return@runBlocking dao.delete(item) > 0
    }
    //endregion

    //region enabled
    override fun viewModelUpdateSetEnabled(item: TEntity) = runBlocking {
        return@runBlocking dao.updateSetEnabled(item.id)
    }
    //endregion

    //region view
    override fun viewModelView(id: Int): TEntity? = dao.view(id)
    open fun viewModelView(item: TEntity): TEntity? = dao.view(item.id)
    open fun viewModelViewAll(): LiveData<List<TEntity>> =
        runBlocking {
            return@runBlocking dao.viewAll() ?: MutableLiveData()
        }

    //region withInclude
    fun viewModelView(
        id: Int,
        include: Array<FilterMemberInclude>? = null
    ): TEntity? {
        val data: TEntity = dao.view(id) ?: return null

        // entities:
        include?.forEach {
            if (it.isEntity) setterValueEntity(data, it)
            if (it.isList) {
                setterValueEntityList(data, it)
            }
        }
        return data
    }

    //region setter
    private fun setterValueEntity(data: IEntity, it: FilterMemberInclude) {
        val groupEntity = getGroupingEntity(data, it)
        if (groupEntity == null || !groupEntity.any()) return
        data.toMembers(TypeFilterAnnotation.ENTITY, groupEntity)
            .filter { s -> s.isGroupMappingInclude }
            .forEach { s ->
                val entity =
                    s.getValue(getInstanceOfIViewModelView(s.typeGroupMappingInclude!!))
                if (entity != null) {
                    if (it.isIncludes) {
                        it.includes.forEach { g ->
                            try {
                                if (g.isEntity) setterValueEntity(entity, g)
                                if (g.isList) setterValueEntityList(entity, g)
                            } catch (ex: Exception) {
                            }
                        }
                    }
                    s.setterEntity(data, entity)
                }
            }
    }

    private fun setterValueEntityList(data: IEntity, it: FilterMemberInclude) {
        val groupEntity = getGroupingListEntity(data, it)
        if (groupEntity == null || !groupEntity.any()) return
        data.toMembers(TypeFilterAnnotation.LIST_OF_ENTITY, groupEntity)
            .filter { s -> s.isGroupMappingInclude }
            .forEach { s ->
                val entity =
                    s.getValue(
                        data.id,
                        getInstanceOfIViewModelAllSimpleListIdRelation(s.typeGroupMappingInclude!!)
                    )
                if (entity != null) {
                    if (it.isIncludes) {
                        it.includes.forEach { a ->
                            try {
                                entity.forEach { e ->
                                    try {
                                        if (a.isEntity) setterValueEntity(e, a)
                                        if (a.isList) setterValueEntityList(e, a)
                                    } catch (ex: Exception) {
                                    }
                                }
                            } catch (ex: Exception) {
                            }
                        }
                    }
                    s.setterListEntity(data, entity)
                }
            }
    }
    //endregion

    //region grouping
    private fun getGroupingEntity(
        data: IEntity,
        include: FilterMemberInclude? = null
    ): List<GroupMappingInclude>? {
        if (include == null) return null

        val list: MutableList<MappingInclude> = mutableListOf()
        data.toMembers(TypeFilterAnnotation.ENTITY)
            .forEach {
                list.add(MappingInclude().apply {
                    groupId = it.valueId
                    name = it.name
                    idEntity = it.toInt(data)
                    isIdEntity = it.isLong
                    isEntity = include.javaTypeEntity == it.javaKType
                    type = it.kType
                })
            }

        var groupList: MutableList<GroupMappingInclude>? = null
        if (list.any()) {
            groupList = mutableListOf()
            list.groupBy { it.groupId }
                .filter { it.value.count() == 2 && it.value.count { s -> include.javaTypeEntity == s.javaType } == 1 }
                .forEach {
                    groupList.add(GroupMappingInclude().apply {
                        groupId = it.key
                        idEntity = it.value.firstOrNull { s -> s.isIdEntity }?.idEntity ?: 0
                        name = it.value.firstOrNull { s -> s.isEntity }?.name ?: ""
                        type = it.value.firstOrNull { s -> s.isEntity }?.type
                    })
                }
        }
        return groupList
    }

    private fun getGroupingListEntity(
        data: IEntity,
        include: FilterMemberInclude? = null
    ): List<GroupMappingInclude>? {
        if (include == null) return null

        val list: MutableList<MappingInclude> = mutableListOf()
        data.toMembers(TypeFilterAnnotation.LIST_OF_ENTITY)
            .forEach {
                list.add(MappingInclude().apply {
                    name = it.name
                    isEntity = include.javaTypeList == it.javaKType
                    type = it.kType
                })
            }

        var groupList: MutableList<GroupMappingInclude>? = null
        if (list.any()) {
            groupList = mutableListOf()
            list.groupBy { it.name }
                .forEach {
                    groupList.add(GroupMappingInclude().apply {
                        name = it.key
                        type = it.value.firstOrNull()?.type
                        idEntity = data.id
                    })
                }
        }
        return groupList
    }
    //endregion

    //region viewModelInterface
    protected abstract fun getInstanceOfIViewModelView(type: KType): IViewModelView<*>?
    protected abstract fun getInstanceOfIViewModelAllSimpleListIdRelation(type: KType): IViewModelAllSimpleListIdRelation<*>?
    //endregion

    //endregion

    //endregion

    //region pager config
    private val pageSize = 60
    private val prefetchDistance = pageSize
    private val enablePlaceholders = false
    private val initialLoadSize = pageSize * 3
    private val maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
    private val jumpThreshold: Int = PagingSource.LoadResult.Page.COUNT_UNDEFINED

    protected var pagingConfig = PagingConfig(
        pageSize,
        prefetchDistance,
        enablePlaceholders,
        initialLoadSize,
        maxSize,
        jumpThreshold
    )

    fun viewModelDefaultPaging(): PagingData<TEntity> = PagingData.from(listOf())
    //endregion

    //region check
    override fun checkExistsEntity(entity: TEntity?): Boolean {
        var data: Boolean
        if (entity == null) {
            onRaiseException("La entidad no puede ser nula...", -1)
            return false
        }
        data = dao.checkExitsEntity(entity.id)
        try {
            if (!data) onRaiseException(
                "El id de la entidad ${entity.javaClass.simpleName} no existe...",
                -2
            )
        } catch (e: Exception) {
            data = false
        }
        return data
    }

    override fun isEntity(entity: TEntity?): Boolean {
        if (entity == null) return false
        return try {
            dao.checkExitsEntity(entity.id)
        } catch (e: Exception) {
            false
        }
    }
    //endregion

    //region utilHandler
    protected fun onProcess(message: String) {
        if (message.isEmpty()) return
        onEventProcess?.invoke(message)
    }
    //endregion

    //region exceptions
    protected fun getIdForHelp(value: Int? = null): String =
        "#${this.javaClass.simpleName} -> ${
            when (value == null) {
                false -> value.toPadStart(10)
                true -> ""
            }
        }"

    protected fun onRaiseException(message: String?, idHelp: String? = null) {
        if (message.isNullOrEmpty()) return
        val sb = StringBuilder()
        sb.append(message)
        sb.append("...")
        if (!idHelp.isNullOrEmpty()) {
            sb.appendLine()
            sb.append("#id: ")
            sb.append(idHelp)
        }
        throw Exception(sb.toString())
    }

    protected fun onRaiseException(message: String?, idHelp: Int? = null) {
        onRaiseException(message, getIdForHelp(idHelp))
    }

    protected fun onRaiseException(message: Exception?, idHelp: String? = null) {
        if (message == null) return
        onRaiseException(message.message, idHelp)
    }

    protected fun onRaiseException(message: Exception?, idHelp: Int? = null) {
        if (message == null) return
        onRaiseException(message.message, idHelp)
    }
    //endregion

    //region extension
    private fun IEntity.toMembers(
        type: TypeFilterAnnotation,
        names: List<GroupMappingInclude>? = null
    ): List<MappingMembers> {
        var data: MutableList<MappingMembers> = mutableListOf()
        this::class.members
            .filter { it.hasAnnotation<EntityForeignKeyID>() || it.hasAnnotation<EntityForeignKeyList>() }
            .mapTo(data) {
                MappingMembers(it).apply {
                    notationEntityID = it.findAnnotation()
                    notationEntityList = it.findAnnotation()
                }
            }
        if (!data.any()) return data
        data = when (type) {
            TypeFilterAnnotation.ENTITY -> data.filter { it.isEntityId }.toMutableList()
            TypeFilterAnnotation.LIST_OF_ENTITY -> data.filter { it.isEntityList }.toMutableList()
            TypeFilterAnnotation.TITLE_UI -> mutableListOf()
        }
        if (names != null && names.any()) {
            data = data.filter {
                names.groupBy { g -> g.name }.map { g -> g.key }.contains(it.name)
            }.toMutableList()

            val filterName: MutableList<MappingMembers> = mutableListOf()
            data.mapTo(filterName) {
                it.setGroupMappingInclude(names.firstOrNull { s -> s.name == it.name })
                it
            }
            return filterName
        }
        return data
    }

    private enum class TypeFilterAnnotation {
        ENTITY,
        LIST_OF_ENTITY,

        // TODO: resolver
        TITLE_UI
    }
    //endregion
}