package common.model.master.company

import android.graphics.Bitmap
import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.common.model.IEntityPicture
import com.vsg.helper.common.model.ItemBasePaging
import com.vsg.helper.helper.array.HelperArray.Companion.toBitmap
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.grayScale
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R

@Entity(tableName = Company.ENTITY_NAME)
class Company : ItemBasePaging<Company>(), IEntityPicture {
    var name: String = ""

    //region picture
    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    override var picture: ByteArray? = null
    override val isBitmap: Boolean
        get() = !(picture == null || picture!!.isEmpty())

    @Ignore
    override fun getPictureShow(): Bitmap? = when (picture == null || picture!!.isEmpty()) {
        true -> null
        false -> {
            val bitmap = this.picture.toBitmap()
            when (isEnabled) {
                true -> bitmap
                false -> bitmap?.grayScale()
            }
        }
    }

    @Ignore
    @DrawableRes
    override fun getDrawableShow(): Int = R.drawable.pic_company
    //endregion

    //region reference
    override val title: String
        get() = name

    override fun aTitleRecyclerAdapter(): String = "$name ${
        when (hasItems) {
            true -> "*"
            false -> ""
        }
    }"

    override fun aBitmapRecyclerAdapter(): Bitmap? = getPictureShow()

    override fun aTitlePopUpData(): String = name
    override fun descriptionView(): Spanned =
        StringBuilder().toTitleSpanned(name)
            .getBaseDescriptionView().castToHtml()

    override fun reference(): Spanned {
        return StringBuilder().toTitleSpanned(name)
            .getBaseDescriptionView().castToHtml()
    }

    override fun aEquals(other: Any?): Boolean {
        if (other !is Company) return false
        return name == other.name
                && picture.contentEquals(other.picture)
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "company"
    }
}