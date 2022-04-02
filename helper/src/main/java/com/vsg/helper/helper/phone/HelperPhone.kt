package com.vsg.helper.helper.phone

import android.telephony.PhoneNumberUtils
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.util.*


class HelperPhone {
    companion object {
        fun countriesRegions(): MutableList<CodeCountryPhone> {
            val map: MutableList<CodeCountryPhone> = mutableListOf()
            PhoneNumberUtil.getInstance().apply {
                Locale.getISOCountries().forEach {
                    val locale = Locale("es", it)
                    val temp = locale.toCodeCountryPhone(this)
                    if (temp != null) map.add(temp)
                }
            }
            return map.sortedBy { it.country }.toMutableList()
        }

        fun countriesRegionsSpa(): MutableList<CodeCountryPhone> {
            val map: MutableList<CodeCountryPhone> = mutableListOf()
            val countries: List<String> = listOf(
                "es-ar", "es-bo", "es-cl", "es-co", "es-cr", "es-do", "es-ec",
                "es-sv", "es-gt", "es-hn", "es-mx", "es-ni", "es-pa", "es-py",
                "es-pe", "es-pr", "es-es", "es-uy", "es-ve", "pt-br"
            )
            PhoneNumberUtil.getInstance().apply {
                var id: Int = 0
                Locale.getAvailableLocales().filter {
                    countries.map { s -> s.lowercase(Locale.ROOT) }.contains(
                        it.toLanguageTag().lowercase(
                            Locale.ROOT
                        )
                    )
                }.sortedBy { it.country }.forEach {
                    val temp = it.toCodeCountryPhone(this, ++id)
                    if (temp != null) map.add(temp)
                }
            }
            return map
        }

        fun Locale.toCodeCountryPhone(): CodeCountryPhone? =
            this.toCodeCountryPhone(PhoneNumberUtil.getInstance())

        fun Locale.toCodeCountryPhone(phone: PhoneNumberUtil, id: Int = 1): CodeCountryPhone? {
            val temp = CodeCountryPhone(
                this.country,
                phone.getCountryCodeForRegion(this.country),
                this.displayCountry,
                id
            )
            return when (temp.isValid) {
                true -> temp
                false -> null
            }
        }

        fun String.isPhoneNumberValidate(): Boolean {
            var isValid: Boolean
            PhoneNumberUtil.getInstance().apply {
                for (r in this.supportedRegions) {
                    try {
                        // check if it's a possible number
                        isValid = this.isPossibleNumber(this@isPhoneNumberValidate, r)
                        if (isValid) {
                            val number: Phonenumber.PhoneNumber =
                                this.parse(this@isPhoneNumberValidate, r)

                            // check if it's a valid number for the given region
                            isValid = this.isValidNumberForRegion(number, r)
                            if (isValid) {
                                println(r + ": " + number.countryCode + ", " + number.nationalNumber)
                                return true
                            }
                        }
                    } catch (e: NumberParseException) {
                        e.printStackTrace()
                    }
                }
            }
            return false
        }

        fun String.isPhoneNumberValidate(code: String): Boolean {
            if (code.isEmpty()) return false
            var isValid: Boolean
            PhoneNumberUtil.getInstance().apply {
                try {
                    // check if it's a possible number
                    isValid = this.isPossibleNumber(this@isPhoneNumberValidate, code)
                    if (isValid) {
                        val number: Phonenumber.PhoneNumber =
                            this.parse(this@isPhoneNumberValidate, code)

                        // check if it's a valid number for the given region
                        isValid = this.isValidNumberForRegion(number, code)
                        if (isValid) {
                            return true
                        }
                    }
                } catch (e: NumberParseException) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun String.formatPhoneNumber(code: String): String {
            if (this.isEmpty() || code.isEmpty()) return this
            return PhoneNumberUtils.formatNumber(this, code)
        }
    }
}