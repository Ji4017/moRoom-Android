package com.moroom.android.presentation.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {
    private val _idValid = MutableLiveData<Boolean>()
    val idValid: LiveData<Boolean>
        get() = _idValid

    private val _passwordValid = MutableLiveData<Boolean>()
    val passwordValid: LiveData<Boolean>
        get() = _passwordValid

    private val _isFormValid = MutableLiveData<Boolean>(false)
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    fun validateId(id: String) {
        _idValid.value = Patterns.EMAIL_ADDRESS.matcher(id).matches()
        updateFormState()
    }

    fun validatePassword(password: String) {
        _passwordValid.value = password.length >= 6
        updateFormState()
    }

    private fun updateFormState() {
        _isFormValid.value = (_idValid.value == true && _passwordValid.value == true)
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { login ->
                _loginResult.value = login.isSuccessful
            }
    }
}
