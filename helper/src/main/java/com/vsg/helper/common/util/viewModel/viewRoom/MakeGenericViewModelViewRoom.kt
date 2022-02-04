package com.vsg.helper.common.util.viewModel.viewRoom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.common.model.EntityForeignKeyList
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import com.vsg.helper.common.util.dao.viewRoom.IGenericDaoPagingRelationViewRoom
import com.vsg.helper.common.util.viewModel.*
import com.vsg.helper.common.util.viewModel.util.FilterMemberInclude
import com.vsg.helper.common.util.viewModel.util.GroupMappingInclude
import com.vsg.helper.common.util.viewModel.util.MappingInclude
import com.vsg.helper.common.util.viewModel.util.MappingMembers
import com.vsg.helper.helper.Helper.Companion.toPadStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

abstract class MakeGenericViewModelViewRoom<TDao, TEntity, TViewRoom>(
    protected val dao: TDao,
    val context: Application
) :
    AndroidViewModel(context),
    IViewModelAllPagingViewRoom<TEntity>,
    IViewModelAllSimpleListViewRoom<TEntity>,
    IViewModelAllTextSearchRelation
        where TDao : IGenericDaoPagingRelationViewRoom<TEntity, TViewRoom>,
              TEntity : IEntityViewRoom<TEntity>,
              TViewRoom : IEntity {

    //region handler
    var onEventProcesando: ((String) -> Unit)? = null
    //endregion

    //region view
    override fun viewModelGetViewAllPaging(idRelation: Int) = runBlocking {
        var i = 0
        val data: Flow<PagingData<TEntity>> = Pager(
            pagingConfig,
            0,
            dao.viewAllPagingViewRoom(idRelation).map {
                val data = viewRoomToEntity(it)
                data.id = i++
                data
            }
                .asPagingSourceFactory()
        ).flow
        return@runBlocking data
    }

    override fun viewModelViewAllSimpleList(idRelation: Int): List<TEntity> {
        var i = 0
        val temp = dao.viewAllListViewRoom(idRelation)
        return when (temp == null) {
            true -> emptyList()
            false -> temp.map {
                val data = viewRoomToEntity(it)
                data.id = i++
                data
            }
        }
    }

    override fun viewModelGetAllTextSearch(idRelation: Int): LiveData<List<String>> =
        dao.viewGetAllTextSearch(idRelation)

    abstract fun viewRoomToEntity(item: TViewRoom): TEntity
    //endregion


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

    //region utilHandler
    protected fun onProcess(message: String) {
        if (message.isEmpty()) return
        onEventProcesando?.invoke(message)
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
        LIST_OF_ENTITY
    }
    //endregion
}