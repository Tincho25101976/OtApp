package com.vsg.helper.helper.exception

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.ui.adapter.IDataAdapterEnum

class HelperException {
    companion object {
        fun IDataAdapterEnum.throwException(data: String) {
            if (!this.isException) return
            when (data.isEmpty()) {
                true -> throw Exception("El tipo no puede ser ${this.title}...")
                false -> throw Exception("$data no puede ser ${this.title}...")
            }
        }

        fun <T> T?.throwException(data: String, male: Boolean = true) where T : IEntity {
            if (this != null) return
            val result: String = "nul${
                when (male) {
                    true -> "o"
                    false -> "a"
                }
            }"
            when (data.isEmpty()) {
                true -> throw Exception("El tipo no puede ser $result...")
                false -> throw Exception("$data no puede ser $result...")
            }
        }
        fun Number.throwExceptionSmallThanZero(data: String){
            if (this.toDouble() < 0) {
                when (data.isEmpty()) {
                    true -> throw Exception("debe ser un numero positivo......")
                    false -> throw Exception("$data debe ser un numero positivo......")
                }
            }
        }
        fun Number.throwExceptionSmallOrEqualThanZero(data: String){
            if (this.toDouble() <= 0) {
                when (data.isEmpty()) {
                    true -> throw Exception("debe ser un numero positivo......")
                    false -> throw Exception("$data debe ser un numero positivo......")
                }
            }
        }
        fun Number.throwExceptionGreaterThanZero(data: String){
            if (this.toDouble() > 0) {
                when (data.isEmpty()) {
                    true -> throw Exception("debe ser un numero negativo......")
                    false -> throw Exception("$data debe ser un numero negativo......")
                }
            }
        }
        fun Number.throwExceptionGreaterOrEqualThanZero(data: String){
            if (this.toDouble() >= 0) {
                when (data.isEmpty()) {
                    true -> throw Exception("debe ser un numero negativo......")
                    false -> throw Exception("$data debe ser un numero negativo......")
                }
            }
        }
        fun Number.throwExceptionEqualThanZero(data: String){
            if (this.toDouble() == 0.0) {
                when (data.isEmpty()) {
                    true -> throw Exception("debe ser un numero negativo......")
                    false -> throw Exception("$data debe ser un numero negativo......")
                }
            }
        }
        fun Number.throwExceptionId(){
            this.throwExceptionSmallOrEqualThanZero("El id")
        }
        fun String.throwException(data: String, male: Boolean = true) {
            if (this.isNotEmpty()) return
            val result: String = "nul${
                when (male) {
                    true -> "o"
                    false -> "a"
                }
            }"
            when (data.isEmpty()) {
                true -> throw Exception("El campo no puede ser $result...")
                false -> throw Exception("$data no puede ser $result...")
            }
        }
    }
}