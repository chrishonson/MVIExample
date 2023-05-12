package com.adammcneilly.mviexample.login.domain

import android.util.Log
import com.adammcneilly.mviexample.redux.Reducer

/**
 * This reducer is responsible for handling any [LoginAction], and using that to create
 * a new [LoginState].
 */
class LoginReducer : Reducer<LoginState, LoginAction> {

    /**
     * Side note: Notice that all of the functions are named in a way that they signify they're
     * returning a new state, and not just processing information. This helps keep your when statements
     * clear that they're returning stuff, so that context isn't lost.
     */
    override fun reduce(currentState: LoginState, action: LoginAction): LoginState {
        val newState = when (action) {
            is LoginAction.EmailChanged -> {
                stateWithNewEmail(currentState, action)
            }
            is LoginAction.PasswordChanged -> {
                stateWithNewPassword(currentState, action)
            }
            LoginAction.LoginStarted -> {
                stateAfterLoginStarted(currentState)
            }
            LoginAction.LoginCompleted -> {
                stateAfterLoginCompleted(currentState)
            }
            is LoginAction.LoginFailed -> {
                stateAfterLoginFailed(currentState)
            }
            LoginAction.InvalidEmailSubmitted -> {
                stateWithInvalidEmailError(currentState)
            }
            else -> currentState
        }
        Log.v(
            "LoginReducer",
            "Processing action: $action; NEW state: $newState"
        )
        return newState
    }

    private fun stateWithInvalidEmailError(currentState: LoginState) =
        currentState.copy(
            emailError = "Please enter an email address.",
        )

    private fun stateAfterLoginStarted(currentState: LoginState) =
        currentState.copy(
            showProgressBar = true,
        )

    private fun stateAfterLoginCompleted(currentState: LoginState) =
        currentState.copy(
            showProgressBar = false,
            loginSuccess = true
        )

    private fun stateAfterLoginFailed(currentState: LoginState) =
        currentState.copy(
            showProgressBar = false,
        )

    private fun stateWithNewPassword(
        currentState: LoginState,
        action: LoginAction.PasswordChanged
    ) = currentState.copy(
        password = action.newPassword,
    )

    private fun stateWithNewEmail(
        currentState: LoginState,
        action: LoginAction.EmailChanged
    ) = currentState.copy(
        email = action.newEmail,
    )
}