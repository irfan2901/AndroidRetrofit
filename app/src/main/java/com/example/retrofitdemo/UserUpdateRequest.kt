package com.example.retrofitdemo

data class UserUpdateRequest(
    val username: String?,
    val email: String?,
    val password: String?
)