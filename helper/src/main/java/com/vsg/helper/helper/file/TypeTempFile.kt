package com.vsg.helper.helper.file

enum class TypeTempFile(var data: TypeTempValue) {
    DATABASE(TypeTempValue("temp_database_", "xml")),
    IMAGE_SCREENSHOT_PNG(TypeTempValue("screenShot_", "png")),
    IMAGE_SCREENSHOT_JPEG(TypeTempValue("screenShot_", "jpeg")),
    IMAGE_CAMERA(TypeTempValue("camera_", "jpg")),
    IMAGE_CHOOSER_FILE(TypeTempValue("picture_", "jpg")),
    REPORT_PDF_FILE(TypeTempValue("report", "pdf"))
}