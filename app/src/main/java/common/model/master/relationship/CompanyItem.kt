package common.model.master.relationship

import androidx.room.Embedded
import androidx.room.Relation
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem

data class CompanyItem(
    @Embedded
    val parent: MasterCompany,
    @Relation(
        parentColumn = "id",
        entityColumn = "idCompany"
    )
    val children: List<MasterItem>
)
//    : EntityOtRelationShip<MasterItem>

//data class CompanyItem(
//    override val parent: MasterCompany, override val children: List<MasterItem>
//) : EntityOtRelationShip<MasterItem>(parent, children)