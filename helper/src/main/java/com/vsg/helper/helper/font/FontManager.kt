package com.vsg.helper.helper.font

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.text.style.TypefaceSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntRange
import com.vsg.helper.ui.widget.ICustomViewSetTypeface


class FontManager(val context: Context) {

    private var data: List<Components>

    init {
        data = listOf(
            Components(Typeface.NORMAL, "KOMTXTK_.ttf"),
            Components(Typeface.BOLD_ITALIC, "KOMTXTBI.ttf"),
            Components(Typeface.BOLD, "KOMTXTKB.ttf"),
            Components(Typeface.ITALIC, "KOMTXTKI.ttf")
        )
    }

    @SuppressLint("Range")
    fun replaceFonts(
        viewTree: ViewGroup,
        @IntRange(from = -1, to = Typeface.BOLD_ITALIC.toLong()) style: Int = -1
    ) {
        val childCount = viewTree.childCount
        for (i in 0 until childCount) {
            when (val child: View = viewTree.getChildAt(i)) {
                is ViewGroup -> replaceFonts(child)
                is ICustomViewSetTypeface -> {
                    child.customSetTypeface(getTypeface(Typeface.BOLD_ITALIC))
                }
                is TextView -> {
                    try {
                        child.typeface = when (style == -1) {
                            true -> getTypeface(child.typeface)
                            false -> getTypeface(style)
                        }
                        child.setTextColor(Color.BLACK)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getTypeface(
        @IntRange(
            from = Typeface.NORMAL.toLong(),
            to = Typeface.BOLD_ITALIC.toLong()
        ) type: Int
    ): Typeface {
        return data.first { it.type == type }.getTypeface(this.context)
    }

    private fun getTypeface(type: Typeface): Typeface = getTypeface(Integer.valueOf(type.style))

    private class Components(val type: Int, val name: String) {
        fun getTypeface(context: Context): Typeface {
            val am: AssetManager = context.assets
            return Typeface.createFromAsset(am, "$SUB_PATH_FONT/${name}")
        }
    }

    companion object Static {
        fun Activity.typeFacePacifico(): Typeface =
            Typeface.createFromAsset(this.assets, "font/Pacifico.ttf")

        fun Activity.typeFaceCustom(type: Int): Typeface = FontManager(this).getTypeface(type)
        fun Activity.typeFaceCustomSpan(type: Int): TypefaceSpan {
            val data = this.typeFaceCustom(type)
            return CustomTypefaceSpan("custom", data)
        }

        const val SUB_PATH_FONT: String = "font"
    }
}