package com.ifs21019.lostfound.data.remote

sealed class MyResult<out R> private constructor() {
    data class Success<out T>(val data: T) : MyResult<T>()
    data class Error(val error: String) : MyResult<Nothing>()
    data object Loading : MyResult<Nothing>()
}