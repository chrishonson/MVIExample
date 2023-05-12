package com.adammcneilly.mviexample.login.data

interface LoginRepository {
    suspend fun login(email: String, password: String): Boolean
}
