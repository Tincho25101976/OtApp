package com.vsg.helper.helper.progress

interface ICallbackProgress {
    var onProgress: ((Int, Int, Double) -> Unit)?
}