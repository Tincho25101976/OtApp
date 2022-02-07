package common.model.std.metodo

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.model.init.entity.EntityOt
import common.model.master.company.MasterCompany
import common.model.std.ensayo.StdEnsayo

@Entity(tableName = StdMetodo.ENTITY_NAME)
class StdMetodo : EntityOt<StdMetodo>() {

    //region properties
    @Ignore
    val stdEnsayo: MutableList<StdEnsayo> = mutableListOf()
    //endregion

    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_default

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is MasterCompany) return false
        return description == other.description
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "std_metodo"
    }
}