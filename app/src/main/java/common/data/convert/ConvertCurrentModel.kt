package common.data.convert

import androidx.room.TypeConverter
import com.vsg.helper.common.utilEnum.IValue
import common.model.master.batch.type.TypeBatchStatus
import common.model.master.item.type.TypePlant
import common.model.master.item.type.TypeProduct
import common.model.master.section.type.TypeSection
import com.vsg.helper.util.unit.type.TypeUnit

class ConvertCurrentModel {
    //region generic
    private fun <TEnum> enumToInt(data: TEnum): Int where TEnum : Enum<TEnum>, TEnum : IValue =
        data.value

    private inline fun <reified TEnum> intToEnum(data: Int): TEnum where TEnum : Enum<TEnum>, TEnum : IValue =
        enumValues<TEnum>().first { it.value == data }
    //enumValues<TEnum>()[data]
    //endregion

    //region enum
    @TypeConverter
    fun typeToInt(data: TypeBatchStatus): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeBatchStatus(data: Int): TypeBatchStatus = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypePlant): Int = enumToInt(data)

    @TypeConverter
    fun intToTypePlant(data: Int): TypePlant = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypeProduct): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeProduct(data: Int): TypeProduct = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypeSection): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeSection(data: Int): TypeSection = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypeUnit): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeUnit(data: Int): TypeUnit = intToEnum(data)
    //endregion
}