package com.moroom.android.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.moroom.android.domain.repository.AuthRepository
import com.moroom.android.domain.usecase.validation.ValidateIdUseCase
import com.moroom.android.domain.usecase.validation.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateIdUseCase: ValidateIdUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    private val _idValid = MutableLiveData<Boolean>()
    val idValid: LiveData<Boolean>
        get() = _idValid

    private val _passwordValid = MutableLiveData<Boolean>()
    val passwordValid: LiveData<Boolean>
        get() = _passwordValid

    private val _isFormValid = MediatorLiveData<Boolean>(false).apply {
        addSource(_idValid) { updateFormState() }
        addSource(_passwordValid) { updateFormState() }
    }
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>>
        get() = _loginResult

    fun validateId(email: String) {
        _idValid.value = validateIdUseCase(email)
    }

    fun validatePassword(password: String) {
        _passwordValid.value = validatePasswordUseCase(password)
    }

    private fun updateFormState() {
        _isFormValid.value = (_idValid.value == true && _passwordValid.value == true)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = authRepository.login(email, password)
        }
    }
}
