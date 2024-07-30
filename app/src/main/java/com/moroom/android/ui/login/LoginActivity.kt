package com.moroom.android.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.moroom.android.R
import com.moroom.android.databinding.ActivityLoginBinding
import com.moroom.android.ui.nav.MainActivity
import com.moroom.android.ui.signup.SignupActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListener()
        setUpObserver()
    }

    private fun setUpListener() {
        binding.etId.addTextChangedListener { loginViewModel.validateId(it.toString().trim().lowercase()) }

        binding.etPassword.addTextChangedListener { loginViewModel.validatePassword(it.toString().trim()) }

        binding.btLogin.setOnClickListener {
            loginViewModel.login(
                binding.etId.text.toString().trim().lowercase(), binding.etPassword.text.toString().trim()
            )
        }

        binding.tvSignUp.setOnClickListener { navigateToSignup() }
    }

    private fun setUpObserver() {
        loginViewModel.idValid.observe(this) { isValid ->
            if(isValid) binding.etId.error = null
            else binding.etId.error = getString(R.string.invalid_email)
        }

        loginViewModel.passwordValid.observe(this) { isValid ->
            if(isValid) binding.etPassword.error = null
            else binding.etPassword.error = getString(R.string.invalid_password)
        }

        loginViewModel.isFormValid.observe(this) {binding.btLogin.isEnabled = it }

        loginViewModel.loginResult.observe(this) { isLoggedIn: Boolean ->
            if (isLoggedIn) {
                Toast.makeText(this, R.string.welcome, Toast.LENGTH_SHORT).show()
                navigateToMain()
            } else {
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}