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
    fun intToType(data: Int): TypeBatchStatus = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypePlant): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeMoney(data: Int): TypePlant = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypeProduct): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeSection(data: Int): TypeProduct = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypeSection): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeOperationStatus(data: Int): TypeSection = intToEnum(data)

    @TypeConverter
    fun typeToInt(data: TypeUnit): Int = enumToInt(data)

    @TypeConverter
    fun intToTypeOperation(data: Int): TypeUnit = intToEnum(data)
    //endregion
}