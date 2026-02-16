package com.example.mobileapplication.api

import com.example.mobileapplication.model.LoginRequest
import com.example.mobileapplication.model.RegisterRequest
import com.example.mobileapplication.model.UserResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Any>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @GET("api/auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserResponse>

    @POST("api/auth/logout")
    suspend fun logout(@Query("token") token: String): Response<Any>
}