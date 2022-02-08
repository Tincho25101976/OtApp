package common.model.std.ensayo

import androidx.annotation.DrawableRes
import androidx.room.*
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import common.helper.HelperMaster.Companion.toUnit
import common.model.common.unit.Unit
import common.model.common.unit.type.TypeUnit
import common.model.init.entity.EntityOt
import common.model.std.common.IStdIdKeyEnsayo
import common.model.master.company.MasterCompany
import common.model.std.ensayo.type.TypeEnsayo
import common.model.std.especificacion.StdEspecificacion
import common.model.std.metodo.StdMetodo

@Entity(
    foreignKeys =
    [
        ForeignKey(
            entity = StdMetodo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idMetodo"),
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = arrayOf("id", "idMetodo"), name = "IX_STD_METODO_FK"),
        Index(value = arrayOf("idMetodo", "idKeyEnsayo"), name = "IX_STD_METODO_KEY"),
        Index(
            value = arrayOf("idKeyEnsayo", "typeUnit", "typeEnsayo"),
            name = "IX_STD_METODO_TYPE"
        )],
    inheritSuperIndices = true,
    tableName = StdEnsayo.ENTITY_NAME
)
class StdEnsayo : EntityOt<StdMetodo>(), IStdIdKeyEnsayo {

    //region properties
    override var idKeyEnsayo: String = ""
    var typeUnit: TypeUnit = TypeUnit.UNDEFINED
    var typeEnsayo: TypeEnsayo = TypeEnsayo.UNDEFINED

    //region fk
    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idMetodo: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var metodo: StdMetodo? = null
    //endregion

    @Ignore
    var precision: Int = Unit.DEFAULT_VALUE_PRECISION
        get() = when (field <= 0) {
            true -> Unit.DEFAULT_VALUE_PRECISION
            else -> field
        }
    val unit: Unit get() = typeUnit.toUnit(precision)
    val isCualitativa: Boolean get() = typeEnsayo == TypeEnsayo.CUANTITATIVO

    @Ignore
    val stdEspecificacion: MutableList<StdEspecificacion> = mutableListOf()
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
        const val ENTITY_NAME = "std_ensayo"
    }


}