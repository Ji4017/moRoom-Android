package com.moroom.android.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.moroom.android.R
import com.moroom.android.databinding.ActivityLoginBinding
import com.moroom.android.ui.navui.MainActivity
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
        binding.etId.addTextChangedListener { loginViewModel.validateId(it.toString().trim()) }

        binding.etPassword.addTextChangedListener { loginViewModel.validatePassword(it.toString().trim()) }

        binding.btLogin.setOnClickListener {
            loginViewModel.login(
                binding.etId.text.toString(), binding.etPassword.text.toString()
            ) }

        binding.tvSignUp.setOnClickListener { navigateToSignup() }
    }

    private fun setUpObserver() {
        loginViewModel.idError.observe(this) { binding.etId.error = it?.let { getString(it) } }

        loginViewModel.passwordError.observe(this) { binding.etPassword.error = it?.let { getString(it) } }

        loginViewModel.isFormValid.observe(this) {binding.btLogin.isEnabled = it }

        loginViewModel.loginResult.observe(this) { loginResult: Boolean ->
            if (loginResult) {
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