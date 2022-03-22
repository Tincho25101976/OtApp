package com.vsg.helper.helper.phone

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.helper.phone.HelperPhone.Companion.formatPhoneNumber
import com.vsg.helper.helper.phone.HelperPhone.Companion.isPhoneNumberValidate
import com.vsg.helper.ui.adapter.IDataAdapterTitle

data class CodeCountryPhone(
    val code: String, val countryNumber: Int, val country: String,
    override var id: Int = 1
) : IDataAdapterTitle, IEntity {
    val codeCountry: String
        get() = "+$countryNumber"
    val isValid: Boolean
        get() = code.isNotEmpty() && countryNumber > 0 && country.isNotEmpty()

    override val title: String
        get() = country

    var number: String = ""
        get() = codeCountry + field

    val numberFormat: String
        get() = number.formatPhoneNumber(country)

    fun isNumberValid(): Boolean = number.isPhoneNumberValidate(code)
}