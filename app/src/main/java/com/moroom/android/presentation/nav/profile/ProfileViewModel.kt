package com.moroom.android.presentation.nav.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _isUserDeleted = MutableLiveData<Boolean>()
    val isUserDeleted: LiveData<Boolean>
        get() = _isUserDeleted

    private val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    fun logout() = auth.signOut()

    fun deleteUserData() {
        viewModelScope.launch {
            val dbDeletionResult = deleteUserFromDatabase()
            val authDeletionResult = deleteUserAuthentication()
            _isUserDeleted.value = (dbDeletionResult && authDeletionResult)
        }
    }

    private suspend fun deleteUserFromDatabase(): Boolean {
        val database = FirebaseDatabase.getInstance()
        val dbDeletionResult = CompletableDeferred<Boolean>()

        currentUser?.let { user ->
            database.getReference("UserAccount").child(user.uid).removeValue()
                .addOnCompleteListener { task ->
                    dbDeletionResult.complete(task.isSuccessful)
                }
        } ?: dbDeletionResult.complete(false)

        return dbDeletionResult.await()
    }

    private suspend fun deleteUserAuthentication(): Boolean {
        val authDeletionResult = CompletableDeferred<Boolean>()

        currentUser?.let { currentUser ->
            currentUser.delete().addOnCompleteListener { task ->
                authDeletionResult.complete(task.isSuccessful)
            }
        } ?: authDeletionResult.complete(false)

        return authDeletionResult.await()
    }
}