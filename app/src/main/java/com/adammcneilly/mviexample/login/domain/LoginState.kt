package com.adammcneilly.mviexample.login.domain

import com.adammcneilly.mviexample.redux.State

/**
 * An implementation of [State] that describes the configuration of the login screen at a given time.
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val showProgressBar: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginSuccess: Boolean = false,
) : State