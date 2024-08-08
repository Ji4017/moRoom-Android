package com.moroom.android.di

import com.moroom.android.data.repository.AccountRepositoryImpl
import com.moroom.android.data.repository.AuthRepositoryImpl
import com.moroom.android.data.repository.ReviewRepositoryImpl
import com.moroom.android.data.repository.CheckItemRepositoryImpl
import com.moroom.android.data.repository.CoordinatesRepositoryImpl
import com.moroom.android.domain.repository.AccountRepository
import com.moroom.android.domain.repository.AuthRepository
import com.moroom.android.domain.repository.CheckItemRepository
import com.moroom.android.domain.repository.CoordinatesRepository
import com.moroom.android.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindCheckItemRepository(checkItemRepositoryImpl: CheckItemRepositoryImpl): CheckItemRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindCoordinatesRepository(coordinatesRepositoryImpl: CoordinatesRepositoryImpl): CoordinatesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}
