package com.vsg.helper.common.adapter

class DataAdapter(var title: String, var body: String) : IDataAdapter {
    override fun titleAdapter(): String? = title
    override fun bodyAdapter(): String = body
}