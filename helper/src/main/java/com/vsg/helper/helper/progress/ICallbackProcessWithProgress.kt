package com.vsg.helper.helper.progress

interface ICallbackProcessWithProgress<T> : ICallbackProgress {
    fun processWithProgress(data: List<T>): Boolean
}