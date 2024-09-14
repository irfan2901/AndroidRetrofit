package com.example.retrofitdemo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/auth/register")
    fun registerUser(@Body user: User): Call<RegisterResponse>

    @GET("/api/users/me")
    fun getCurrentUser(@Header("Authorization") authHeader: String): Call<User>

    @PUT("api/users/update/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String,
        @Body user: UserUpdateRequest
    ): Call<UserResponse>

    @DELETE("/api/users/delete/{id}")
    fun deleteUser(@Path("id") id: Int, @Header("Authorization") authHeader: String): Call<String>
}