package com.moroom.android.presentation.signup

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.moroom.android.data.source.remote.model.UserAccount
import com.moroom.android.domain.repository.AccountRepository
import com.moroom.android.domain.repository.AuthRepository
import com.moroom.android.domain.usecase.validation.ValidateEmailDomainUseCase
import com.moroom.android.domain.usecase.validation.ValidateIdUseCase
import com.moroom.android.domain.usecase.validation.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository,
    private val validateEmailDomainUseCase: ValidateEmailDomainUseCase,
    private val validateIdUseCase: ValidateIdUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _domainValid = MutableLiveData<Boolean>()
    val domainValid: LiveData<Boolean>
        get() = _domainValid

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

    private val _emailSendResult = MutableLiveData<Boolean>()
    val emailSendResult: LiveData<Boolean>
        get() = _emailSendResult

    private val _dynamicLinkEvent = MutableLiveData<Boolean>()
    val dynamicLinkEvent: LiveData<Boolean>
        get() = _dynamicLinkEvent

    private val _signupResult = MutableLiveData<Int>()
    val signupResult: LiveData<Int>
        get() = _signupResult

    fun validateEmailDomain(email: String) {
        _domainValid.value = validateEmailDomainUseCase(email)
    }

    fun validateId(id: String) {
        _idValid.value = validateIdUseCase(id)
    }

    fun validatePassword(password: String) {
        _passwordValid.value = validatePasswordUseCase(password)
    }

    private fun updateFormState() {
        _isFormValid.value = (_idValid.value == true && _passwordValid.value == true)
    }

    fun sendEmail(email: String) {
        viewModelScope.launch { _emailSendResult.value = authRepository.sendEmail(email) }
    }

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            var resultCode = accountRepository.createUserInAuthentication(email, password)
            if (resultCode == 0) {
                resultCode = accountRepository.createUserInDB(email, password)
            }

            _signupResult.value = resultCode
        }
    }

    fun handleDeepLink(intent: Intent) {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData: PendingDynamicLinkData? ->
                if (pendingDynamicLinkData != null) {
                    val deepLink = pendingDynamicLinkData.link
                    if (deepLink != null) {
                        _dynamicLinkEvent.value = true
                    }
                }
            }
            .addOnFailureListener { exception: Exception? ->
                Log.e(TAG, "getDynamicLink:onFailure", exception)
                _dynamicLinkEvent.value = false
            }
    }
}