package common.model.master.relationship

import common.model.init.entity.EntityOtRelationShip
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem

//data class CompanyItem(
//    @Embedded
//    override val parent: MasterCompany,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "idCompany"
//    )
//    override val children: List<MasterItem>
//) : EntityOtRelationShip<MasterItem>

data class CompanyItem(
    override val parent: MasterCompany, override val children: List<MasterItem>
) : EntityOtRelationShip<MasterItem>(parent, children)