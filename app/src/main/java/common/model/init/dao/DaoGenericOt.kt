package common.model.init.dao

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IGenericDaoPaging

abstract class DaoGenericOt<T> : IGenericDaoPaging<T> where T : IResultRecyclerAdapter,
                                                            T : IEntityPagingLayoutPosition,
                                                            T : IEntity,
                                                            T : IIsEnabled,
                                                            T : Comparable<T> {

}