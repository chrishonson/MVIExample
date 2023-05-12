package com.adammcneilly.mviexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adammcneilly.mviexample.login.ui.LoginFragment
import com.adammcneilly.mviexample.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment())
                .commitNow()
        }
    }

    /**
     * To navigate to the profile screen, we'll run a Fragment transaction to replace the login
     * screen with the profile screen.
     */
    fun navigateToProfile() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProfileFragment())
            .commitNow()
    }
}