package com.combo.runcombi.common

sealed interface DomainResult<out T> {
    data class Success<out T>(val data: T) :
        DomainResult<T>

    data class Error(val code: Int? = null, val message: String? = null) :
        DomainResult<Nothing>

    data class Exception(val error: Throwable) :
        DomainResult<Nothing>
}

fun <T, R> DomainResult<T>.convert(
    convert: (T) -> R,
): DomainResult<R> {
    return when (this) {
        is DomainResult.Success -> {
            DomainResult.Success(convert(this.data))
        }

        is DomainResult.Error -> {
            DomainResult.Error(code = this.code, message = this.message)
        }

        is DomainResult.Exception -> {
            DomainResult.Exception(error = this.error)
        }
    }
}
