package com.moroom.android.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.auth.FirebaseAuth
import com.moroom.android.R

class LoginViewModel : ViewModel() {
    private val _idError = MutableLiveData<Int?>()
    val idError: LiveData<Int?>
        get() = _idError

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?>
        get() = _passwordError

    private val _isFormValid = MutableLiveData<Boolean>(false)
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    fun validateId(id: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
            _idError.value = R.string.invalid_email
        } else {
            _idError.value = null
        }

        updateFormState()
    }

    fun validatePassword(password: String) {
        if (password.length < 6) {
            _passwordError.value = R.string.invalid_password
        } else {
            _passwordError.value = null
        }

        updateFormState()
    }

    private fun updateFormState() {
        _isFormValid.value = (_idError.value == null && _passwordError.value == null)
    }

    fun login(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { login ->
                _loginResult.value = login.isSuccessful
            }
    }
}
