package com.vsg.helper.common.model

import android.graphics.Bitmap
import android.text.Spanned
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.vsg.helper.common.popup.IPopUpData
import com.vsg.helper.common.popup.IResultPopUpData
import com.vsg.helper.common.popup.PopUpData
import com.vsg.helper.helper.Helper.Companion.toSiNo
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

abstract class ItemBase : IIsEnabled, IIsDefault, IDescription, IEntity, IResultPopUpData,
    IDescriptionView, IEntityKeySearch {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override var id: Int = 0

    @ColumnInfo(name = "isDefault")
    override var isDefault: Boolean = false

    @ColumnInfo(name = "isEnabled")
    override var isEnabled: Boolean = true

    @ColumnInfo(name = "description")
    override var description: String = ""

    override val allowDefaultValue: Boolean
        get() = true

    override val isEntityOnlyDefault: Boolean
        get() = false

    override val keySearch: String get() = id.toPadStart(10)

    @Ignore
    protected abstract fun aTitlePopUpData(): String

    @Ignore
    protected open fun oBodyPopUpData(): StringBuilder = StringBuilder()

    @Ignore
    protected open fun aBitmapPopUpData(): Bitmap? = null

    @Ignore
    protected fun StringBuilder.getBaseDescriptionView(): StringBuilder {
//        if (description.isNotEmpty()) this.toLineSpanned("Descripción", description)
        this.toLineSpanned("Activo", isEnabled.toSiNo())
        this.toLineSpanned("Defecto", isDefault.toSiNo())
        return this
    }

    fun extendedToHTMLPopUp(data: StringBuilder): Spanned {
        return oBodyPopUpData()
            .append(data)
            .getBaseDescriptionView().castToHtml()
    }

    @Ignore
    override fun getPopUp(): IPopUpData = PopUpData(aTitlePopUpData(), "").apply {
        this.toHtml = oBodyPopUpData()
            .getBaseDescriptionView().castToHtml()
        this.factorHeight = 0.45
        this.bitmap = aBitmapPopUpData()
    }

    @Ignore
    protected fun formatId(): String = id.toString().padStart(5, '0')

    //region reflection
    fun getFields(): List<String> {
        val lst = listOf("id", "valueCode", "tag")
        val ttt = this::class.memberProperties.filter { lst.contains(it.name) }.toList()
        val result = this::class.memberProperties.filter { it.findAnnotation<Ignore>() != null }
            .map { it.name }
        val resultNotIgnore = this::class.memberProperties.filter {
            it.findAnnotation<Ignore>() == null
                    && it.isAccessible
        }.map { it.name }
        val data = this::class.memberProperties.map { it.name }.toList()
        return result
    }
    //endregion

    companion object {

    }
}