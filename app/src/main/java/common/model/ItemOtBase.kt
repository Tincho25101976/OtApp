package common.model

import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.room.Ignore
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.model.ItemBasePagingAuditingCode
import com.vsg.helper.common.model.util.DrawableShow
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.ot.R

abstract class ItemOtBase<T> : ItemBasePagingAuditingCode<T>()
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {

    //region properties
    override val isEntityOnlyDefault: Boolean
        get() = true
    //endregion

    //region methods
    @Ignore
    override fun aTitleRecyclerAdapter(): String = title

    @Ignore
    @DrawableRes
    open fun oGetDrawablePicture(): Int = R.drawable.pic_default

    @Ignore
    fun getPictureRecyclerAdapter(): DrawableShow =
        DrawableShow(oGetDrawablePicture(), this.isEnabled)

    @Ignore
    override fun getDrawableShow(): DrawableShow =
        DrawableShow(oGetDrawablePicture(), this.isEnabled)

    @Ignore
    override fun aTitlePopUpData(): String = title

    @Ignore
    open fun oGetSpannedGeneric(): StringBuilder = StringBuilder()
    override fun descriptionView(): Spanned =
        oGetSpannedGeneric().getBaseDescriptionView().castToHtml()

    override fun reference(): Spanned = oGetSpannedGeneric().getBaseDescriptionView().castToHtml()
    //endregion
}