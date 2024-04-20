package com.ifs21019.lostfound.data.repository

import com.google.gson.Gson
import com.ifs21019.lostfound.data.remote.MyResult
import com.ifs21019.lostfound.data.remote.response.LostandFoundResponse
import com.ifs21019.lostfound.data.remote.retrofit.IApiService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class ObjectRepository private constructor(
    private val apiService: IApiService,
) {
    fun postObject(
        title: String,
        description: String,
        status: String
    ) = flow {
        emit(MyResult.Loading)
        try {
//get success message
            emit(
                MyResult.Success(
                    apiService.postObject(title, description, status).data
                )
            )
        } catch (e: HttpException) {
//get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, LostandFoundResponse::class.java)
                        .message
                )
            )
        }
    }
    fun putObject(
        objectId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ) = flow {
        emit(MyResult.Loading)
        try {
//get success message
            emit(
                MyResult.Success(
                    apiService.putObject(
                        objectId,
                        title,
                        description,
                        if (isCompleted) 1 else 0
                    )
                )
            )
        } catch (e: HttpException) {
//get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, LostandFoundResponse::class.java)
                        .message
                )
            )
        }
    }
    fun getObjects(
        isCompleted: Int?,
    ) = flow {
        emit(MyResult.Loading)
        try {
//get success message
            emit(MyResult.Success(apiService.getObjects(isCompleted)))
        } catch (e: HttpException) {
//get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, LostandFoundResponse::class.java)
                        .message
                )
            )
        }
    }
    fun getObject(
        objectId: Int,
    ) = flow {
        emit(MyResult.Loading)
        try {
//get success message
            emit(MyResult.Success(apiService.getObject(objectId)))
        } catch (e: HttpException) {
//get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, LostandFoundResponse::class.java)
                        .message
                )
            )
        }
    }
    fun deleteObject(
        objectId: Int,
    ) = flow {
        emit(MyResult.Loading)
        try {
//get success message
            emit(MyResult.Success(apiService.deleteObject(objectId)))
        } catch (e: HttpException) {
//get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, LostandFoundResponse::class.java)
                        .message
                )
            )
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ObjectRepository? = null
        fun getInstance(
            apiService: IApiService,
        ): ObjectRepository {
            synchronized(ObjectRepository::class.java) {
                INSTANCE = ObjectRepository(
                    apiService
                )
            }
            return INSTANCE as ObjectRepository
        }
    }
}