package common.model.init.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.entity.IEntityRelationShip
import common.model.master.company.MasterCompany

abstract class EntityOtRelationShip<TChildren>(
    @Embedded
    override val parent: MasterCompany,
    @Relation(
        parentColumn = "id",
        entityColumn = "idCompany"
    )
    override val children: List<TChildren>
) : IEntityRelationShip<MasterCompany, TChildren>
        where TChildren : IEntity