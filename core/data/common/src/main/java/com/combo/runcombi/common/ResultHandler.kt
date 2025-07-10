package com.combo.runcombi.common

import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> handleResult(execute: suspend () -> Response<T>): DomainResult<T> {
    return try {
        val response = execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                DomainResult.Success(body)
            } else {
                DomainResult.Error(response.code(), "Response body is null")
            }
        } else {
            DomainResult.Error(response.code(), response.errorBody()?.string())
        }
    } catch (e: HttpException) {
        DomainResult.Error(e.code(), e.message())
    } catch (e: Throwable) {
        DomainResult.Exception(e)
    }
}
