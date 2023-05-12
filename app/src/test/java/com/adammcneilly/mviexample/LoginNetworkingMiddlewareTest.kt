package com.adammcneilly.mviexample

import com.adammcneilly.mviexample.login.domain.LoginAction
import com.adammcneilly.mviexample.login.domain.LoginNetworkingMiddleware
import com.adammcneilly.mviexample.login.domain.LoginReducer
import com.adammcneilly.mviexample.login.domain.LoginState
import com.adammcneilly.mviexample.redux.Store
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LoginNetworkingMiddlewareTest {

    /**
     * When running this test, we want to ensure that after having [LoginNetworkingMiddleware] process
     * the [LoginAction.SignInButtonClicked] action when the email is invalid, it should then
     * have the store process the [LoginAction.InvalidEmailSubmitted] action.
     */
    @Test
    fun submitInvalidEmail() {
        // Givens
        val fakeLoginRepository = FakeLoginRepository()
        fakeLoginRepository.shouldMockSuccess = true

        val inputState = LoginState(email = "")
        val inputAction = LoginAction.SignInButtonClicked

        val middlewareUnderTest = LoginNetworkingMiddleware(fakeLoginRepository)
        val actionCaptureMiddleware = ActionCaptureMiddleware<LoginState, LoginAction>()

        val loginStore = Store(
            inputState,
            LoginReducer(),
            listOf(actionCaptureMiddleware),
        )

        // When
        runBlocking {
            middlewareUnderTest.process(inputAction, inputState, loginStore)
        }

        // Then
        actionCaptureMiddleware.assertActionProcessed(LoginAction.InvalidEmailSubmitted)
    }

    /**
     * When the [LoginNetworkingMiddleware] processes the [LoginAction.SignInButtonClicked] action
     * and there are valid inputs, we want to ensure that the corresponding actions are sent back
     * into the store.
     */
    @Test
    fun validLogin() {
        // Givens
        val fakeLoginRepository = FakeLoginRepository()
        fakeLoginRepository.shouldMockSuccess = true

        val inputState = LoginState(
            email = "testy@mctestface.com",
            password = "hunter2",
        )
        val inputAction = LoginAction.SignInButtonClicked

        val middlewareUnderTest = LoginNetworkingMiddleware(fakeLoginRepository)
        val actionCaptureMiddleware = ActionCaptureMiddleware<LoginState, LoginAction>()

        val loginStore = Store(
            inputState,
            LoginReducer(),
            listOf(actionCaptureMiddleware),
        )

        // When
        runBlocking {
            middlewareUnderTest.process(inputAction, inputState, loginStore)
        }

        // Then
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginStarted)
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginCompleted)
    }

    @Test
    fun loginFailure() {
        // Givens
        val fakeLoginRepository = FakeLoginRepository()
        fakeLoginRepository.shouldMockSuccess = false

        val inputState = LoginState(
            email = "testy@mctestface.com",
            password = "hunter2",
        )
        val inputAction = LoginAction.SignInButtonClicked

        val middlewareUnderTest = LoginNetworkingMiddleware(fakeLoginRepository)
        val actionCaptureMiddleware = ActionCaptureMiddleware<LoginState, LoginAction>()

        val loginStore = Store(
            inputState,
            LoginReducer(),
            listOf(actionCaptureMiddleware),
        )

        // When
        runBlocking {
            middlewareUnderTest.process(inputAction, inputState, loginStore)
        }

        // Then
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginStarted)
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginFailed(null))
    }
}
