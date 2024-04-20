package com.ifs21019.lostfound.data.remote.retrofit

import com.ifs21019.lostfound.data.remote.response.LostandFoundResponse
import com.ifs21019.lostfound.data.remote.response.LostFoundAddObjectResponse
import com.ifs21019.lostfound.data.remote.response.LostFoundLoginResponse
import com.ifs21019.lostfound.data.remote.response.LostFoundResponse
import com.ifs21019.lostfound.data.remote.response.LostFoundUserResponse
import com.ifs21019.lostfound.data.remote.response.LostFoundsResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): LostandFoundResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LostFoundLoginResponse
    @GET("users/me")
    suspend fun getMe(): LostFoundUserResponse

    @FormUrlEncoded
    @POST("lost-founds")
    suspend fun postObject(
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("status") status: String
    ): LostFoundAddObjectResponse
    @FormUrlEncoded
    @PUT("lost-founds/{id}")
    suspend fun putObject(
        @Path("id") objectId: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("isCompleted") isCompleted: Int,
    ): LostandFoundResponse
    @GET("lost-founds")
    suspend fun getObjects(
        @Query("isCompleted") isCompleted: Int?,
    ): LostFoundsResponse
    @GET("lost-founds/{id}")
    suspend fun getObject(
        @Path("id") objectId: Int,
    ): LostFoundResponse
    @DELETE("lost-founds/{id}")
    suspend fun deleteObject(
        @Path("id") objectId: Int,
    ): LostandFoundResponse
}