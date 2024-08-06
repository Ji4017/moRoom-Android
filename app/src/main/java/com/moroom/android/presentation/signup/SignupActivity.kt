package com.moroom.android.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.moroom.android.R
import com.moroom.android.databinding.ActivitySignUpBinding
import com.moroom.android.presentation.nav.MainActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleDeepLink()
        setUpListener()
        setUpObserver()
    }

    private fun handleDeepLink() = viewModel.handleDeepLink(intent)

    private fun setUpListener() {
        binding.apply {
            etEmailAddress.onFocusChangeListener = OnFocusChangeListener { _: View, hasFocus: Boolean -> if (hasFocus) setUpDomain() }

            etEmailAddress.addTextChangedListener { viewModel.validateEmailDomain(it.toString().trim()) }

            etId.addTextChangedListener { viewModel.validateId(it.toString().trim().lowercase()) }

            etPassword.addTextChangedListener { viewModel.validatePassword(it.toString().trim()) }

            btSendEmail.setOnClickListener { viewModel.sendEmail(etEmailAddress.text.toString().trim()) }

            btSignUp.setOnClickListener { viewModel.createUserInAuthentication(
                etId.text.toString().trim().lowercase(),
                etPassword.text.toString().trim())
            }
        }
    }

    private fun setUpObserver() {
        viewModel.domainValid.observe(this) {isValid ->
            if(isValid) binding.etEmailAddress.error = null
            else binding.etEmailAddress.error = getString(R.string.only_cju_email)

            binding.btSendEmail.isEnabled = isValid
        }

        viewModel.idValid.observe(this) {isValid ->
            if(isValid) binding.etId.error = null
            else binding.etId.error = getString(R.string.invalid_email)
        }

        viewModel.passwordValid.observe(this) {isValid ->
            if(isValid) binding.etPassword.error = null
            else binding.etPassword.error = getString(R.string.invalid_password)
        }

        viewModel.isFormValid.observe(this) { binding.btSignUp.isEnabled = it }

        viewModel.emailSendResult.observe(this) { showSendResultToast(it) }

        viewModel.dynamicLinkEvent.observe(this) { isSuccess -> if (isSuccess) showSignUpForm() }

        viewModel.signupResult.observe(this) { signupResult ->
            when (signupResult) {
                0 -> {
                    Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                1 -> Toast.makeText(this, getString(R.string.in_use_email), Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this, getString(R.string.signup_error), Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(this, getString(R.string.singup_error_inquiry), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpDomain() {
        binding.etEmailAddress.setText("@" + getString(R.string.domain))
        binding.etEmailAddress.post { binding.etEmailAddress.setSelection(0) }
    }

    private fun showSendResultToast(emailSendResult: Boolean) {
        if (emailSendResult) Toast.makeText(this, getString(R.string.sending_success), Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, getString(R.string.sending_failure), Toast.LENGTH_SHORT).show()
    }

    private fun showSignUpForm() {
        binding.apply {
            etEmailAddress.visibility = View.GONE
            btSendEmail.visibility = View.GONE
            etId.visibility = View.VISIBLE
            etPassword.visibility = View.VISIBLE
            btSignUp.visibility = View.VISIBLE
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}