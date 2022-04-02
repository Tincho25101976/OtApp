package common.model.master.relationship

import androidx.room.Embedded
import androidx.room.Relation
import common.model.master.batch.MasterBatch
import common.model.master.company.MasterCompany

data class CompanyBatch(
    @Embedded
    val parent: MasterCompany,
    @Relation(
        parentColumn = "id",
        entityColumn = "idCompany"
    )
    val children: List<MasterBatch>
)
//    : IEntityRelationShip<MasterCompany, MasterBatch>

//data class CompanyBatch(
//    @Embedded
//    override val parent: MasterCompany,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "idCompany"
//    )
//    override val children: List<MasterBatch>
//) : IEntityRelationShip<MasterCompany, MasterBatch>