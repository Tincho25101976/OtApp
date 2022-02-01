package common.model.master.relationship

import androidx.room.Embedded
import androidx.room.Relation
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem

data class CompanyProduct(
    @Embedded
    val company: MasterCompany,
    @Relation(
        parentColumn = "id",
        entityColumn = "idCompany"
    )
    val product: List<MasterItem>
)