package com.adammcneilly.mviexample.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.adammcneilly.mviexample.R
import com.adammcneilly.mviexample.databinding.FragmentLoginBinding
import com.adammcneilly.mviexample.ui.profile.ProfileFragment
import kotlinx.coroutines.flow.collect

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Whenever the view is resumed, subscribe to our viewmodel's view state StateFlow
        lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect { viewState ->
                processViewState(viewState)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Whenever a relevant UI action occurs like a text change or a button click, proxy that
     * to the view model to handle.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailInput.doOnTextChanged { text, _, _, _ ->
            viewModel.emailChanged(text?.toString().orEmpty())
        }

        binding.passwordInput.doOnTextChanged { text, _, _, _ ->
            viewModel.passwordChanged(text?.toString().orEmpty())
        }

        binding.signInButton.setOnClickListener {
            viewModel.signInButtonClicked()
        }
    }

    private fun processViewState(viewState: LoginViewState) {
        binding.progressBar.visibility = if (viewState.showProgressBar) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.emailInputLayout.error = viewState.emailError

        if (viewState.loginSuccess) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment())
                .commitNow()
        }
    }
}