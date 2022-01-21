package com.vsg.helper.ui.popup.parameter

import androidx.annotation.DrawableRes
import com.vsg.helper.common.popup.IPopUpParameter


abstract class UICustomAlertDialogParameterBase<TEnum>(val title: String): IPopUpParameter {
    private val items: MutableList<TEnum> = mutableListOf()
    val titles: HashMap<TEnum, String> = hashMapOf()
    val images: HashMap<TEnum, Int> = hashMapOf()
    abstract fun menu(): List<TEnum>

    var sizeImage: Int = SIZE_DEFAULT_IMAGE_SIZE
    override var factorHeight: Double = 0.32
        get() {
            if (field <= 0.0) return 0.1
            if (field >= 1.0) return 1.0
            return field
        }
    override var factorWidth: Double = 0.8
        get() {
            if (field <= 0.0) return 0.1
            if (field >= 1.0) return 1.0
            return field
        }
    override var canceledOnTouchOutside: Boolean = true
    fun setTitle(action: TEnum, title: String) {
        if (title.isNotEmpty() && this.title.isNotEmpty()) titles[action] = title
    }
    fun setImage(action: TEnum, @DrawableRes image: Int) {
        if (this.title.isNotEmpty()) images[action] = image
    }

    fun addAll(vararg items: TEnum) {
        this.items.clear()
        this.items.addAll(items)
        factorHeight = calculatedFactorHeight()
    }

    protected fun makeMenu(sorted: (TEnum) -> Int): List<TEnum> {
        if (!items.any()) return emptyList()
        return items.groupBy { it }.map { it.key }.sortedBy(sorted)
    }

    fun isProcess(): Boolean = when (items.any()) {
        true -> items.groupBy { it }.map { it.key }.any()
        false -> false
    }

    fun calculatedFactorHeight(size: Int = sizeImage): Double =
        (menu().count() * getFactorHeight(size)) + 0.05 + when (title.isEmpty()) {
            true -> 0.0
            false -> 0.08
        }

    private fun getFactorHeight(size: Int = sizeImage): Double {
        if (size <= 0) return FACTOR_UNIT_HEIGHT
        return size * FACTOR_UNIT_HEIGHT / SIZE_DEFAULT_IMAGE_SIZE
    }

    internal fun getSizeFontWhitFactor(size: Int = sizeImage ): Float {
        if (size <= 0) return SIZE_DEFAULT_TEXT_SIZE
        return size * SIZE_DEFAULT_TEXT_SIZE / SIZE_DEFAULT_IMAGE_SIZE
    }
    internal fun getSizeTitleFontWhitFactor(size: Int = sizeImage ): Float {
        if (size <= 0) return SIZE_DEFAULT_TITLE_TEXT_SIZE
        return size * SIZE_DEFAULT_TITLE_TEXT_SIZE / SIZE_DEFAULT_IMAGE_SIZE
    }

    companion object Static {
        const val FACTOR_UNIT_HEIGHT: Double = 0.045
        const val SIZE_DEFAULT_IMAGE_SIZE: Int = 32
        const val SIZE_DEFAULT_TEXT_SIZE: Float = 16F
        const val SIZE_DEFAULT_TITLE_TEXT_SIZE: Float = 0.95F
    }
}