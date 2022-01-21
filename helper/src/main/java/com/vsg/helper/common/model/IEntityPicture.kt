package com.vsg.helper.common.model

interface IEntityPicture : IEntityDrawable, IEntityBitmap {
    var picture: ByteArray?
}