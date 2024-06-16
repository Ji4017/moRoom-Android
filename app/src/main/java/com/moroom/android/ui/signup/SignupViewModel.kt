package com.moroom.android.ui.signup

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.moroom.android.R
import com.moroom.android.data.model.UserAccount

class SignupViewModel : ViewModel() {
    private val _domainError = MutableLiveData<Int?>()
    val domainError: LiveData<Int?>
        get() = _domainError

    private val _idError = MutableLiveData<Int?>()
    val idError: LiveData<Int?>
        get() = _idError

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?>
        get() = _passwordError

    private val _isFormValid = MutableLiveData<Boolean>(false)
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    private val _emailSendResult = MutableLiveData<Boolean>()
    val emailSendResult: LiveData<Boolean>
        get() = _emailSendResult

    private val _dynamicLinkEvent = MutableLiveData<Boolean>()
    val dynamicLinkEvent: LiveData<Boolean>
        get() = _dynamicLinkEvent

    private val _signupMessage = MutableLiveData<String>()
    val signupMessage: LiveData<String>
        get() = _signupMessage

    fun validateEmailDomain(context: Context, email: String) {
        val domain = email.substringAfter("@")
        val isValidDomain = domain.equals(getString(context, R.string.domain), ignoreCase = true)
        _domainError.value = if(isValidDomain) null else R.string.invalid_email
    }

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

    fun sendEmail(email: String) {
        val actionCodeSettings = buildActionCodeSettings()
        val auth = FirebaseAuth.getInstance()
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    _emailSendResult.value = true
                    Log.d(TAG, "이메일 보내기 성공.")
                } else {
                    _emailSendResult.value = false
                    Log.e(TAG, "이메일 보내기 실패", task.exception)
                }
            }
    }

    fun createUserInAuthentication(context: Context, email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    createUserInDB(context, email, password)
                } else {
                    if (task.exception is FirebaseAuthException) {
                        _signupMessage.value = getString(context, R.string.in_use_email)
                    } else {
                        _signupMessage.value = getString(context, R.string.signup_error)
                    }
                }
            }
    }

    private fun createUserInDB(context: Context, email: String, password: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().getReference("UserAccount")
        assert(user != null)
        val uid = user!!.uid

        val userAccount = UserAccount()
        userAccount.email = email
        userAccount.password = password
        userAccount.idToken = uid
        userAccount.isReview = false
        databaseReference.child(uid)
            .setValue(userAccount) { databaseError: DatabaseError?, _: DatabaseReference? ->
                if (databaseError != null) {
                    _signupMessage.value = databaseError.message + getString(context, R.string.singup_error_inquiry)
                    Log.d(TAG, "유저 정보 DB 저장 실패: " + databaseError.message)
                } else {
                    _signupMessage.value = getString(context, R.string.welcome)
                    Log.d(TAG, "유저 정보 DB 저장 성공")
                }
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
                Log.e(TAG,"getDynamicLink:onFailure", exception)
                _dynamicLinkEvent.value = false
            }
    }

    private fun buildActionCodeSettings(): ActionCodeSettings {
        return ActionCodeSettings.newBuilder()
            .setUrl("https://moroom.page.link/m1r2")
            .setHandleCodeInApp(true)
            .setAndroidPackageName(
                "com.moroom.android", /* androidPackageName */
                false,  /* installIfNotAvailable */
                "12" /* minimumVersion */
            )
            .build()
    }
}