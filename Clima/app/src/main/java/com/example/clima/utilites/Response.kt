package com.example.clima.utilites

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val error: String) : Response<Nothing>()
}