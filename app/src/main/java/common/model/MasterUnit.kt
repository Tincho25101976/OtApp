package common.model

import android.text.Spanned
import androidx.room.Ignore
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R

class MasterUnit : ItemOtBase() {
    //region properties
    var item: String = ""
    //endregion

    //region reference
    @Ignore
    override fun aTitleRecyclerAdapter(): String = item
    @Ignore
    override fun aPictureRecyclerAdapter(): Int = R.drawable.pic_product
    @Ignore
    override fun aTitlePopUpData(): String = item

    override fun descriptionView(): Spanned {
        val sb = StringBuilder()
        sb.toTitleSpanned(item)
        sb.getBaseDescriptionView()
        return sb.castToHtml()
    }
    //endregion
}