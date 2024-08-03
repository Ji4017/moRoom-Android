package com.moroom.android.presentation.signup

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.util.Patterns
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
import com.moroom.android.data.model.UserAccount

class SignupViewModel : ViewModel() {
    private val _domainValid = MutableLiveData<Boolean>()
    val domainValid: LiveData<Boolean>
        get() = _domainValid

    private val _idValid = MutableLiveData<Boolean>()
    val idValid: LiveData<Boolean>
        get() = _idValid

    private val _passwordValid = MutableLiveData<Boolean>()
    val passwordValid: LiveData<Boolean>
        get() = _passwordValid

    private val _isFormValid = MutableLiveData<Boolean>(false)
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
        val domain = email.substringAfter("@")
        _domainValid.value = domain.equals("cju.ac.kr", ignoreCase = true)
    }

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

    fun createUserInAuthentication(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    createUserInDB(email, password)
                } else {
                    if (task.exception is FirebaseAuthException) {
                        _signupResult.value = 1
                    } else {
                        _signupResult.value = 2
                    }
                }
            }
    }

    private fun createUserInDB(email: String, password: String) {
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
                    _signupResult.value = 3
                    Log.d(TAG, "유저 정보 DB 저장 실패: " + databaseError.message)
                } else {
                    _signupResult.value = 0
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