package common.model.std.especificacion

import androidx.annotation.DrawableRes
import androidx.room.*
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.helper.util.unit.Unit
import com.vsg.helper.util.unit.type.TypeUnit
import common.model.init.entity.EntityOt
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.std.common.IStdEspecificacion
import common.model.std.common.type.TypeEtapa
import common.model.std.ensayo.StdEnsayo
import common.model.std.ensayo.type.TypeEnsayo

@Entity(
    foreignKeys =
    [
        ForeignKey(
            entity = StdEnsayo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idEnsayo"),
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = arrayOf("id", "idEnsayo"), name = "IX_STD_ESPECIFICACION_FK"),
        Index(
            value = arrayOf("valueMinimo", "valueMaximo", "revision", "idEtapa"),
            name = "IX_STD_ESPECIFICACION_TYPE"
        )],
    inheritSuperIndices = true,
    tableName = StdEspecificacion.ENTITY_NAME
)
class StdEspecificacion : EntityOt<StdEspecificacion>(), IStdEspecificacion {

    //region properties
    var valueMinimo: Double = 0.0
    var valueMaximo: Double = 0.0
    var obligatorio: Boolean = false
    var certificado: Boolean = false
    var revision: Int = 0

    override var idEtapa: TypeEtapa = TypeEtapa.UNDEFINED

    //region fk
    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idEnsayo: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var ensayo: StdEnsayo? = null

    @EntityForeignKeyID(30)
    @ColumnInfo(index = true)
    var idItem: Int = 0

    @EntityForeignKeyID(30)
    @Ignore
    override var item: MasterItem? = null
    //endregion

    override var idKeyEnsayo: String
        get() = ensayo?.idKeyEnsayo ?: ""
        set(value) { }

    val typeUnit: TypeUnit = ensayo?.typeUnit ?: TypeUnit.UNDEFINED
    val typeEnsayo: TypeEnsayo = ensayo?.typeEnsayo ?: TypeEnsayo.UNDEFINED
    val unit: Unit? get() = ensayo?.unit
    val isCualitativo: Boolean get() = typeEnsayo == TypeEnsayo.CUANTITATIVO

//    val minimo: Qty

    @Ignore
    val stdMedicion: MutableList<StdEspecificacion> = mutableListOf()
    //endregion

    //region methods

    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_default

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is StdEspecificacion) return false
        return description == other.description
                && valueMaximo == other.valueMaximo
                && valueMinimo == other.valueMinimo
                && obligatorio == other.obligatorio
                && certificado == other.certificado
                && revision == other.revision
                && idEtapa == other.idEtapa
                && idEnsayo == other.idEnsayo
                && idItem == other.idItem
                && typeUnit == other.typeUnit
                && typeEnsayo == other.typeEnsayo
                && createDate.time == other.createDate.time
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "std_especificacion"
    }


}