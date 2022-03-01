package com.arxan.simplesimon

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.arxan.simplesimon.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var mAuthJob: Job? = null
    private lateinit var mBinding: ActivityMainBinding
    private val mMainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.password.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        mBinding.btnServerLogin.setOnClickListener { attemptLogin() }

        val fileName = "textfile.txt"
        val fileContent = application.assets.open(fileName).bufferedReader().use{
            it.readText()
        }

        mBinding.titleText.text = "WELCOME TO $fileContent"
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainScope.cancel()
    }

    private fun isPasswordValid(password: String): Boolean {
        // TODO: Replace this with your own logic
        return password.length > 4
    }

    private fun attemptLogin() {
        if (mAuthJob?.isActive == true) {
            return
        }

        // Reset errors.
        mBinding.password.error = null

        // Store values at the time of the login attempt.
        val passwordStr = mBinding.password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            mBinding.password.error = getString(R.string.error_invalid_password)
            focusView = mBinding.password
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthJob = mMainScope.launch {
                val success: Boolean = makeUserLogin(passwordStr)

                showProgress(false)

                if (success) {
                    Log.i("SimpleSimon", "Login success")
                    val intent = Intent(applicationContext, CongratsActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("SimpleSimon", "Incorrect password")
                    mBinding.password.error = getString(R.string.error_incorrect_password)
                    mBinding.password.requestFocus()
                }
            }
        }
    }

    suspend private fun makeUserLogin(password: String): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d("SimpleSimon", "Checking password")
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                return@withContext false
            }

            return@withContext password == "secret"
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        mBinding.loginProgress.visibility = if (show) View.VISIBLE else View.INVISIBLE
        mBinding.loginProgress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        mBinding.loginProgress.visibility = if (show) View.VISIBLE else View.INVISIBLE
                    }
                })
    }
}
