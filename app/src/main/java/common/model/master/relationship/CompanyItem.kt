package common.model.master.relationship

import androidx.room.Embedded
import androidx.room.Relation
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem

data class CompanyItem(
    @Embedded
    val company: MasterCompany,
    @Relation(
        parentColumn = "id",
        entityColumn = "idCompany"
    )
    val items: List<MasterItem>
)