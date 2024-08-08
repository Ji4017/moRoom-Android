package com.moroom.android.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.moroom.android.data.source.remote.datasource.AccountDataSource
import com.moroom.android.data.source.remote.model.UserAccount
import com.moroom.android.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDataSource: AccountDataSource,
    private val firebaseAuth: FirebaseAuth
) :
    AccountRepository {
    override suspend fun updateUserReviewStatus() = accountDataSource.updateUserReviewStatus()

    override suspend fun createUserInAuthentication(email: String, password: String): Int =
        accountDataSource.createUserInAuthentication(email, password)

    override suspend fun createUserInDB(email: String, password: String): Int {
        val userAccount = UserAccount()
        userAccount.email = email
        userAccount.password = password
        userAccount.idToken = firebaseAuth.uid
        userAccount.isReview = false
        val resultCode = accountDataSource.createUserInDB(userAccount)
        return resultCode
    }
}