package com.vsg.helper.ui.layout

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutGridLayout
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutLinealLayout
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.helper.type.TypeMakeLayoutParameter
import com.vsg.helper.ui.util.BaseActivity


class MainItemsLayoutScroll(private val activity: BaseActivity) {
    //region handler
    var onEventGetOnlyActivity: ((BaseActivity, Class<*>?) -> Unit)? = null
    //endregion

    //region properties
    private val mapItems: MutableList<ActionForCardView> = mutableListOf()
    val map: MutableList<ActionForCardView>
        get() = mapItems
    //endregion

    //region default
    private fun Int.toCustomPixel(): Int = this.toPixel(activity)
    private fun Int.toCustomPixelFloat(): Float = this.toCustomPixel().toFloat()
    //endregion

    //region layouts
    private fun getCardView(@DrawableRes picture: Int, textCard: String = DEFAULT_TEXT): CardView {
        val margin: Int = DEFAULT_CARD_VIEW_MARGIN.toCustomPixel()
        val data = CardView(activity).apply {
            layoutParams = makeCustomLayoutGridLayout(
                TypeMakeLayoutParameter.CUSTOM,
                DEFAULT_CARD_VIEW_WIDTH.toCustomPixel(),
                DEFAULT_CARD_VIEW_HEIGHT.toCustomPixel()
            ).apply {
                setMargins(margin, margin, margin, margin)

                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1F)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1F)
            }
            radius = DEFAULT_CARD_VIEW_RADIUS.toCustomPixelFloat()
            elevation = DEFAULT_CARD_VIEW_ELEVATION.toFloat()
        }

        val linearLayout: LinearLayout = LinearLayout(activity).apply {
            layoutParams =
                makeCustomLayoutLinealLayout(TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_MATCH).apply {
                    gravity = Gravity.CENTER
                }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }
        val imageView: ImageView = ImageView(activity).apply {
            layoutParams = makeCustomLayoutLinealLayout(
                TypeMakeLayoutParameter.CUSTOM,
                DEFAULT_IMAGE_VIEW_WIDTH.toCustomPixel(),
                DEFAULT_IMAGE_VIEW_HEIGHT.toCustomPixel()
            ).apply {
                gravity = Gravity.CENTER
            }
            setImageBitmap(BitmapFactory.decodeResource(activity.resources, picture))
        }
        val textView: TextView = TextView(activity).apply {
            layoutParams = makeCustomLayoutLinealLayout()
            text = textCard
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setTextColor(Color.BLACK)
            textSize = DEFAULT_TEXT_SIZE.toFloat()
            typeface = activity.typeFaceCustom(Typeface.BOLD)
        }
        linearLayout.addView(imageView)
        linearLayout.addView(textView)
        data.addView(linearLayout)

        return data
    }

    private fun getGridLayout(): GridLayout {
        val padding: Int = DEFAULT_GRID_LAYOUT_PADDING
        val data: GridLayout = GridLayout(activity).apply {
            layoutParams =
                makeCustomLayoutGridLayout(TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_MATCH).apply {
                    setMargins(10, 10, 10, 10)
                    setPadding(padding, padding, padding, padding)
                }
            alignmentMode = GridLayout.ALIGN_MARGINS
            columnCount = 2
            rowCount = 4
            isColumnOrderPreserved = false
        }
        mapItems.forEach { s ->
            val cardView = getCardView(s.picture, s.text)
            cardView.setOnClickListener {
                when (s.getType) {
                    ActionForCardView.TypeAction.ACTION -> s.action?.invoke()
                    ActionForCardView.TypeAction.DATA_CLASS -> {
                        when (s.typeDataClass) {
                            ActionForCardView.TypeDataClass.ONLY_ACTIVITY -> {
                                onEventGetOnlyActivity?.invoke(activity, s.dataClass!!)
                                activity.loadActivity(s.dataClass!!)
                            }
                            ActionForCardView.TypeDataClass.ONLY_WITH_PARENT,
                            ActionForCardView.TypeDataClass.ONLY_WITH_PARENT_ACTION ->
                                activity.loadActivity(s.dataClass!!, id = s.parentId)
                            ActionForCardView.TypeDataClass.WITH_PARENT_EXTRA,
                            ActionForCardView.TypeDataClass.WITH_PARENT_EXTRA_ACTION ->
                                activity.loadActivity(
                                    s.dataClass!!,
                                    id = s.parentId,
                                    extra = s.extraId
                                )
                            ActionForCardView.TypeDataClass.ONLY_WITH_EXTRA,
                            ActionForCardView.TypeDataClass.ONLY_WITH_EXTRA_ACTION ->
                                activity.loadActivity(s.dataClass!!, id = 0, extra = s.extraId)
                            ActionForCardView.TypeDataClass.UNDEFINED -> Unit
                        }
                    }
                    ActionForCardView.TypeAction.IS_TOAST -> Toast.makeText(
                        activity, s.text, Toast.LENGTH_SHORT
                    ).show()
                    else -> Unit
                }
            }
            data.addView(cardView)
        }
        return data
    }

    private fun getLinealLayout(): LinearLayout {
        return LinearLayout(activity).apply {
            layoutParams =
                makeCustomLayoutLinealLayout(TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_MATCH).apply {
                    orientation = LinearLayout.VERTICAL
                }
        }
    }

    private fun getScrollView(): ScrollView {
        return ScrollView(activity).apply {
            layoutParams =
                makeCustomLayoutLinealLayout(TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_MATCH)
        }
    }

    fun makeLayout(): ScrollView {
        return getScrollView().apply {
            addView(getLinealLayout().apply {
                addView(getGridLayout())
            })
        }
    }
    //endregion

    //region action
    fun add(
        @DrawableRes picture: Int,
        text: String,
        action: (() -> Unit)? = null,
        dataClass: Class<*>? = null,
        isToast: Boolean = false,
        parent: IEntity? = null,
        extra: IEntity? = null,
    ) {
        mapItems.add(
            ActionForCardView(
                picture,
                text,
                action,
                ActionForCardView.TypeDataClassAction.ENTITY
            ).apply {
                this.dataClass = dataClass
                this.isToast = isToast
                this.addParent(parent)
                this.addExtra(extra)
            })
    }

    fun addWithAction(
        @DrawableRes picture: Int,
        text: String,
        action: (() -> Unit)? = null,
        dataClass: Class<*>? = null,
        isToast: Boolean = false,
        parent: (() -> IEntity?)? = null,
        extra: (() -> IEntity?)? = null,
    ) {
        mapItems.add(
            ActionForCardView(
                picture,
                text,
                action,
                ActionForCardView.TypeDataClassAction.ACTION
            ).apply {
                this.dataClass = dataClass
                this.isToast = isToast
                if (parent != null) this.addParent(parent)
                if (extra != null) this.addExtra(extra)
            })

    }

    class ActionForCardView(
        @DrawableRes val picture: Int,
        val text: String,
        val action: (() -> Unit)? = null,
        private val dataClassWithAction: TypeDataClassAction = TypeDataClassAction.UNDEFINED
    ) {
        var dataClass: Class<*>? = null
        var isToast: Boolean = false

        var parent: IEntity? = null
        var extra: IEntity? = null
        private var actionForParent: (() -> IEntity?)? = null
        private var actionForExtra: (() -> IEntity?)? = null

        val parentId: Int
            get() = try {
                if (typeDataClass.hasParent) {
                    if (typeDataClass.hasAction) this.parent = actionForParent?.invoke()
                    when (parent == null) {
                        false -> parent!!.id
                        true -> 0
                    }
                } else 0
            } catch (ex: Exception) {
                0
            }

        val extraId: Int
            get() = try {
                if (typeDataClass.hasExtra) {
                    if (typeDataClass.hasAction) this.extra = actionForExtra?.invoke()
                    when (extra == null) {
                        false -> extra!!.id
                        true -> 0
                    }
                } else 0
            } catch (ex: Exception) {
                0
            }

        val typeDataClass: TypeDataClass
            get() {
                if (!isDataClass) return TypeDataClass.UNDEFINED
                when (getTypeDataClassWithAction) {
                    TypeDataClassAction.ENTITY -> {
                        if (parent == null && extra == null) return TypeDataClass.ONLY_ACTIVITY
                        if (parent != null && extra == null) return TypeDataClass.ONLY_WITH_PARENT
                        if (parent != null && extra != null) return TypeDataClass.WITH_PARENT_EXTRA
                        if (parent == null && extra != null) return TypeDataClass.ONLY_WITH_EXTRA
                    }
                    TypeDataClassAction.ACTION -> {
                        if (actionForParent == null && actionForExtra == null) return TypeDataClass.ONLY_ACTIVITY
                        if (actionForParent != null && actionForExtra == null) return TypeDataClass.ONLY_WITH_PARENT_ACTION
                        if (actionForParent != null && actionForExtra != null) return TypeDataClass.WITH_PARENT_EXTRA_ACTION
                        if (actionForParent == null && actionForExtra != null) return TypeDataClass.ONLY_WITH_EXTRA_ACTION
                    }
                    TypeDataClassAction.UNDEFINED -> return TypeDataClass.UNDEFINED
                }
                return TypeDataClass.UNDEFINED
            }
        private val getTypeDataClassWithAction: TypeDataClassAction
            get() = when (getType) {
                TypeAction.ACTION, TypeAction.IS_TOAST, TypeAction.UNDEFINED -> TypeDataClassAction.UNDEFINED
                TypeAction.DATA_CLASS -> dataClassWithAction
            }

        private val isAction: Boolean
            get() = action != null
        private val isDataClass: Boolean
            get() = dataClass != null

        val getType: TypeAction
            get() {
                var data: TypeAction = TypeAction.UNDEFINED
                if (isAction) data = TypeAction.ACTION
                if (isDataClass) data = TypeAction.DATA_CLASS
                if (isToast) data = TypeAction.IS_TOAST
                return data
            }

        fun addParent(item: IEntity?) {
            if (getType != TypeAction.DATA_CLASS || item == null) return
            parent = item
        }

        fun addParent(item: (() -> IEntity?)) {
            if (getType != TypeAction.DATA_CLASS) return
            actionForParent = item
        }

        fun addExtra(item: IEntity?) {
            if (getType != TypeAction.DATA_CLASS || item == null) return
            extra = item
        }

        fun addExtra(item: (() -> IEntity?)) {
            if (getType != TypeAction.DATA_CLASS) return
            actionForExtra = item
        }

        fun <T : IEntity> castToParent(): T? {
            if (parent == null) return null
            return parent as T
        }

        fun <T : IEntity> castToExtra(): T? {
            if (extra == null) return null
            return extra as T
        }

        enum class TypeAction {
            ACTION,
            DATA_CLASS,
            IS_TOAST,
            UNDEFINED
        }

        enum class TypeDataClass(
            val hasParent: Boolean = false,
            val hasExtra: Boolean = false,
            val hasAction: Boolean = false
        ) {
            ONLY_ACTIVITY,
            ONLY_WITH_PARENT(hasParent = true),
            ONLY_WITH_PARENT_ACTION(hasParent = true, hasAction = true),
            WITH_PARENT_EXTRA(hasParent = true, hasExtra = true),
            WITH_PARENT_EXTRA_ACTION(hasParent = true, hasExtra = true, hasAction = true),
            ONLY_WITH_EXTRA(hasExtra = true),
            ONLY_WITH_EXTRA_ACTION(hasExtra = true, hasAction = true),
            UNDEFINED
        }

        enum class TypeDataClassAction {
            ENTITY,
            ACTION,
            UNDEFINED
        }

    }
    //endregion

    companion object {
        const val DEFAULT_GRID_LAYOUT_PADDING: Int = 14

        const val DEFAULT_CARD_VIEW_WIDTH: Int = 128
        const val DEFAULT_CARD_VIEW_HEIGHT: Int = 172
        const val DEFAULT_CARD_VIEW_MARGIN: Int = 16
        const val DEFAULT_CARD_VIEW_RADIUS: Int = 16
        const val DEFAULT_CARD_VIEW_ELEVATION: Int = 8

        const val DEFAULT_IMAGE_VIEW_WIDTH: Int = 96
        const val DEFAULT_IMAGE_VIEW_HEIGHT: Int = 96

        const val DEFAULT_TEXT: String = "..."
        const val DEFAULT_TEXT_SIZE: Int = 20
    }
}
