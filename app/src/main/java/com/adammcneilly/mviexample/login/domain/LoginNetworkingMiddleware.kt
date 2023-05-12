package com.adammcneilly.mviexample.login.domain

import com.adammcneilly.mviexample.redux.Middleware
import com.adammcneilly.mviexample.redux.Store

class LoginNetworkingMiddleware(
    private val loginRepository: com.example.data.LoginRepository,
) : Middleware<LoginState, LoginAction> {

    override suspend fun process(
        action: LoginAction,
        currentState: LoginState,
        store: Store<LoginState, LoginAction>,
    ) {
        when (action) {
            is LoginAction.SignInButtonClicked -> {
                if (currentState.email.isEmpty()) {
                    store.dispatch(LoginAction.InvalidEmailSubmitted)
                    return
                }

                loginUser(store, currentState)
            }

            else -> {}
        }
    }

    private suspend fun loginUser(
        store: Store<LoginState, LoginAction>,
        currentState: LoginState
    ) {
        store.dispatch(LoginAction.LoginStarted)

        val isSuccessful = loginRepository.login(
            email = currentState.email,
            password = currentState.password,
        )

        if (isSuccessful) {
            store.dispatch(LoginAction.LoginCompleted)
        } else {
            store.dispatch(LoginAction.LoginFailed(null))
        }
    }
}