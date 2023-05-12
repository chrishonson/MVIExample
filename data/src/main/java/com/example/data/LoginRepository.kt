package com.example.data

interface LoginRepository {
    suspend fun login(email: String, password: String): Boolean
}
